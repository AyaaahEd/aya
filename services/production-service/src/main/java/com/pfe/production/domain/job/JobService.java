package com.pfe.production.domain.job;
import com.pfe.production.shared.sequence.SequenceGeneratorService;
import com.pfe.production.domain.audit.AuditLogService;

import com.pfe.production.domain.form.Form;
import com.pfe.production.domain.formversion.FormVersion;
import com.pfe.production.domain.form.FormRepository;
import com.pfe.production.domain.formversion.FormVersionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;

import java.util.List;
import java.util.Optional;
import org.axonframework.eventhandling.gateway.EventGateway;
import com.pfe.production.shared.exception.BusinessException;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final FormVersionRepository formVersionRepository;
    private final FormRepository formRepository;
    private final SequenceGeneratorService sequenceGeneratorService;
    private final AuditLogService auditLogService;
    private final EventGateway eventGateway;
    private final JobValidator jobValidator;

    public JobService(JobRepository jobRepository, FormVersionRepository formVersionRepository,
            FormRepository formRepository, SequenceGeneratorService sequenceGeneratorService,
            AuditLogService auditLogService, EventGateway eventGateway, JobValidator jobValidator) {
        this.jobRepository = jobRepository;
        this.formVersionRepository = formVersionRepository;
        this.formRepository = formRepository;
        this.sequenceGeneratorService = sequenceGeneratorService;
        this.auditLogService = auditLogService;
        this.eventGateway = eventGateway;
        this.jobValidator = jobValidator;
    }

    @Transactional
    public Job createJob(com.pfe.production.domain.job.CreateJobRequest request) {
        // 1. Validate FormVersion
        FormVersion formVersion = formVersionRepository.findById(request.formVersionId())
                .filter(fv -> !Boolean.TRUE.equals(fv.getDeleted()))
                .orElseThrow(() -> new IllegalArgumentException("FormVersion not found: " + request.formVersionId()));

        // 2. Validate Form
        Form form = formRepository.findById(formVersion.getFormId())
                .orElseThrow(() -> new IllegalArgumentException("Form not found for version: " + formVersion.getId()));

        // 3. Generate Job Number
        String jobNumber = sequenceGeneratorService.generateJobNumber();

        // 4. Build Step Tracking
        List<Job.StepTracking> stepTrackings = new ArrayList<>();
        if (form.getSteps() != null) {
            for (Form.Step formStep : form.getSteps()) {
                Job.StepTracking tracking = new Job.StepTracking();
                tracking.setStep(formStep.getName());
                tracking.setState(Job.StepState.NOT_STARTED);

                // Assign Machine for PRINTING from FormVersion Planning
                if ("PRINTING".equalsIgnoreCase(formStep.getName()) && formVersion.getPlanning() != null) {
                    tracking.setMachineId(formVersion.getPlanning().getMachineId());
                    tracking.setMachineName(formVersion.getPlanning().getMachineName());
                }

                stepTrackings.add(tracking);
            }
        }

        Job job = new Job();
        job.setJobNumber(jobNumber);
        job.setOrderItemId(request.orderItemId());
        job.setOrderItemNumber(request.orderItemNumber());
        job.setFormVersionId(request.formVersionId());
        job.setFormNumber(form.getFormNumber());
        job.setQuantity(request.quantity());
        job.setState(Job.JobState.NOT_STARTED);
        job.setReprint(request.reprint());
        job.setGroupedId(request.groupedId());
        job.setJobPaletteId(request.jobPaletteId());
        job.setStepTracking(stepTrackings);
        job.setCreatedBy("system");
        job.setCreatedAt(LocalDateTime.now());
        job.setUpdatedAt(LocalDateTime.now());

        Job savedJob = jobRepository.save(job);
        auditLogService.log(savedJob.getId(), "JOB", "CREATE_JOB", "Job created with number " + jobNumber, "system");

        // Publish Axon Event
        eventGateway.publish(new JobCreatedEvent(
                savedJob.getId(),
                savedJob.getJobNumber(),
                savedJob.getOrderItemId(),
                savedJob.getCreatedAt()));

        return savedJob;
    }

    @Transactional
    public Job startJob(String id) {
        Job job = jobRepository.findById(id)
                .filter(j -> !Boolean.TRUE.equals(j.getDeleted()))
                .orElseThrow(() -> new IllegalArgumentException("Job not found: " + id));

        // Use domain behavior method
        job.start();

        if (job.getStepTracking() != null && !job.getStepTracking().isEmpty()) {
            auditLogService.log(id, "JOB", "START_STEP", "Started step: " + job.getStepTracking().get(0).getStep(),
                    "system");
        }

        Job savedJob = jobRepository.save(job);
        auditLogService.log(id, "JOB", "START_JOB", "Job started", "system");
        return savedJob;
    }

    @Transactional
    public Job completeStep(String id, String stepName, Integer actualQuantity, String notes) {
        Job job = jobRepository.findById(id)
                .filter(j -> !Boolean.TRUE.equals(j.getDeleted()))
                .orElseThrow(() -> new IllegalArgumentException("Job not found: " + id));

        List<Job.StepTracking> steps = job.getStepTracking();
        Job.StepTracking currentStep = null;

        for (int i = 0; i < steps.size(); i++) {
            if (steps.get(i).getStep().equalsIgnoreCase(stepName)) {
                currentStep = steps.get(i);
                // currentIndex = i; // Removed unused variable
                break;
            }
        }

        if (currentStep == null) {
            throw new IllegalArgumentException("Step not found: " + stepName);
        }

        // Validate Quantity
        if (actualQuantity != null && actualQuantity > job.getQuantity()) {
            if (actualQuantity > job.getQuantity()) {
                throw new IllegalArgumentException("Actual quantity (" + actualQuantity
                        + ") cannot exceed planned quantity (" + job.getQuantity() + ")");
            }
        }

        // Validate Step State
        if (currentStep.getState() != Job.StepState.IN_PROGRESS) {
            throw new IllegalStateException("Step " + stepName + " must be IN_PROGRESS to complete.");
        }

        // Complete current step
        currentStep.setState(Job.StepState.COMPLETED);
        currentStep.setEndDate(LocalDateTime.now());
        currentStep.setActualQuantity(actualQuantity);
        if (notes != null) {
            currentStep.setNotes(notes);
        }

        auditLogService.log(id, "JOB", "COMPLETE_STEP", "Completed step: " + stepName, "system");

        // Check for job completion
        boolean allCompleted = steps.stream().allMatch(s -> s.getState() == Job.StepState.COMPLETED);
        if (allCompleted) {
            job.setState(Job.JobState.COMPLETED);
            auditLogService.log(id, "JOB", "COMPLETE_JOB", "Job fully completed", "system");
        }

        job.setUpdatedAt(LocalDateTime.now());
        return jobRepository.save(job);
    }

    @Transactional
    public Job startNextStep(String id, String machineId) {
        Job job = jobRepository.findById(id)
                .filter(j -> !Boolean.TRUE.equals(j.getDeleted()))
                .orElseThrow(() -> new IllegalArgumentException("Job not found: " + id));

        List<Job.StepTracking> steps = job.getStepTracking();
        Job.StepTracking nextStep = null;
        int nextIndex = -1;

        // Find first NOT_STARTED step
        for (int i = 0; i < steps.size(); i++) {
            if (steps.get(i).getState() == Job.StepState.NOT_STARTED) {
                nextStep = steps.get(i);
                nextIndex = i;
                break;
            }
        }

        if (nextStep == null) {
            // Maybe check if any are IN_PROGRESS?
            throw new IllegalStateException("No next step available to start. All steps are finished or in progress.");
        }

        // Verify previous step is COMPLETED (unless it's the first step)
        if (nextIndex > 0) {
            Job.StepTracking prevStep = steps.get(nextIndex - 1);
            if (prevStep.getState() != Job.StepState.COMPLETED) {
                throw new IllegalStateException("Previous step " + prevStep.getStep() + " is not completed yet.");
            }
        }

        nextStep.setState(Job.StepState.IN_PROGRESS);
        nextStep.setStartDate(LocalDateTime.now());
        if (machineId != null) {
            nextStep.setMachineId(machineId);
        }

        job.setUpdatedAt(LocalDateTime.now());
        auditLogService.log(id, "JOB", "START_STEP", "Started step: " + nextStep.getStep(), "system");

        return jobRepository.save(job);
    }

    @Transactional
    public Job startStep(String id, String stepName) {
        Job job = jobRepository.findById(id)
                .filter(j -> !Boolean.TRUE.equals(j.getDeleted()))
                .orElseThrow(() -> new IllegalArgumentException("Job not found: " + id));

        List<Job.StepTracking> steps = job.getStepTracking();
        for (int i = 0; i < steps.size(); i++) {
            Job.StepTracking step = steps.get(i);
            if (step.getStep().equalsIgnoreCase(stepName)) {
                if (i > 0) {
                    Job.StepTracking prev = steps.get(i - 1);
                    if (prev.getState() != Job.StepState.COMPLETED) {
                        throw new IllegalStateException(
                                "Previous step " + prev.getStep() + " must be completed first.");
                    }
                }
                step.setState(Job.StepState.IN_PROGRESS);
                step.setStartDate(LocalDateTime.now());
                job.setUpdatedAt(LocalDateTime.now());
                auditLogService.log(id, "JOB", "START_STEP", "Started step: " + stepName, "system");
                return jobRepository.save(job);
            }
        }
        throw new IllegalArgumentException("Step not found: " + stepName);
    }

    public List<Job> getJobsByFormVersion(String formVersionId) {
        return jobRepository.findByFormVersionId(formVersionId);
    }

    public Optional<Job> getJobById(String id) {
        return jobRepository.findById(id).filter(j -> !Boolean.TRUE.equals(j.getDeleted()));
    }

    @Transactional
    public void reportMachineFailure(String jobId, String machineId, String reason) {
        Job job = jobRepository.findById(jobId)
                .filter(j -> !Boolean.TRUE.equals(j.getDeleted()))
                .orElseThrow(() -> new IllegalArgumentException("Job not found: " + jobId));

        // In a real scenario, we might pause the job or update its status.
        // For now, we just raise the exception to log it in the Error domain.
        throw new BusinessException(
                "Machine failure on job " + job.getJobNumber() + ": " + reason,
                "MACHINE_FAILURE",
                "MACHINE",
                machineId);
    }
}