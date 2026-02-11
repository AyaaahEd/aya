package com.pfe.production.service;

import com.pfe.production.domain.FormVersion;
import com.pfe.production.domain.Job;
import com.pfe.production.domain.JobPalette;
import com.pfe.production.dto.CreatePaletteRequest;
import com.pfe.production.repository.FormVersionRepository;
import com.pfe.production.repository.JobPaletteRepository;
import com.pfe.production.repository.JobRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class JobPaletteService {

    private final JobPaletteRepository jobPaletteRepository;
    private final JobRepository jobRepository;
    private final FormVersionRepository formVersionRepository;
    private final SequenceGeneratorService sequenceGeneratorService;
    private final AuditLogService auditLogService;

    public JobPaletteService(JobPaletteRepository jobPaletteRepository, JobRepository jobRepository,
            FormVersionRepository formVersionRepository, SequenceGeneratorService sequenceGeneratorService,
            AuditLogService auditLogService) {
        this.jobPaletteRepository = jobPaletteRepository;
        this.jobRepository = jobRepository;
        this.formVersionRepository = formVersionRepository;
        this.sequenceGeneratorService = sequenceGeneratorService;
        this.auditLogService = auditLogService;
    }

    @Transactional
    public JobPalette createPalette(CreatePaletteRequest request) {
        // 1. Validate max jobs
        if (request.jobIds().size() > 10) {
            throw new IllegalArgumentException("Maximum 10 jobs allowed per palette");
        }

        // 2. Fetch Jobs and Validate Compatibility
        List<JobPalette.JobInfo> jobInfos = new ArrayList<>();
        String machineName = null;

        for (int i = 0; i < request.jobIds().size(); i++) {
            String jobId = request.jobIds().get(i);
            Job job = jobRepository.findById(jobId)
                    .filter(j -> !Boolean.TRUE.equals(j.getDeleted()))
                    .orElseThrow(() -> new IllegalArgumentException("Job not found: " + jobId));

            // Check State
            if (job.getState() != Job.JobState.NOT_STARTED && job.getState() != Job.JobState.IN_PROGRESS) {
                throw new IllegalStateException("Job " + job.getJobNumber() + " must be NOT_STARTED or IN_PROGRESS");
            }

            // Check if already in a palette
            if (job.getJobPaletteId() != null) {
                throw new IllegalStateException(
                        "Job " + job.getJobNumber() + " is already assigned to palette " + job.getJobPaletteId());
            }

            // Check Machine Compatibility (via FormVersion)
            FormVersion fv = formVersionRepository.findById(job.getFormVersionId())
                    .orElseThrow(() -> new IllegalArgumentException("FormVersion not found for job: " + jobId));

            if (fv.getPlanning() == null || !request.machineId().equals(fv.getPlanning().getMachineId())) {
                throw new IllegalStateException(
                        "Job " + job.getJobNumber() + " is not planned for machine " + request.machineId());
            }

            if (machineName == null) {
                machineName = fv.getPlanning().getMachineName();
            }

            JobPalette.JobInfo info = new JobPalette.JobInfo();
            info.setJobId(job.getId());
            info.setJobNumber(job.getJobNumber());
            info.setFormNumber(job.getFormNumber());
            info.setQuantity(job.getQuantity());
            info.setPosition(i + 1);
            jobInfos.add(info);
        }

        // 3. Create Palette
        JobPalette palette = new JobPalette();
        palette.setPaletteNumber(sequenceGeneratorService.generatePaletteNumber());
        palette.setMachineId(request.machineId());
        palette.setMachineName(machineName);
        palette.setJobs(jobInfos);
        palette.setState(JobPalette.PaletteState.CREATED);
        palette.setCreatedAt(LocalDateTime.now());
        palette.setUpdatedAt(LocalDateTime.now());
        palette.setCreatedBy("system");

        JobPalette savedPalette = jobPaletteRepository.save(palette);

        // 4. Update Jobs with Palette ID
        for (String jobId : request.jobIds()) {
            Job job = jobRepository.findById(jobId).orElseThrow();
            job.setJobPaletteId(savedPalette.getId());
            jobRepository.save(job);
        }

        auditLogService.log(savedPalette.getId(), "PALETTE", "CREATE_PALETTE",
                "Palette created with " + jobInfos.size() + " jobs", "system");
        return savedPalette;
    }

    @Transactional
    public JobPalette validatePalette(String id) {
        JobPalette palette = jobPaletteRepository.findById(id)
                .filter(p -> !Boolean.TRUE.equals(p.getDeleted()))
                .orElseThrow(() -> new IllegalArgumentException("Palette not found: " + id));

        if (palette.getState() != JobPalette.PaletteState.CREATED) {
            throw new IllegalStateException("Palette must be in CREATED state to validate");
        }

        palette.setState(JobPalette.PaletteState.VALIDATED);
        palette.setUpdatedAt(LocalDateTime.now());

        JobPalette saved = jobPaletteRepository.save(palette);
        auditLogService.log(saved.getId(), "PALETTE", "VALIDATE_PALETTE", "Palette validated", "system");
        return saved;
    }

    @Transactional
    public JobPalette generatePdf(String id) {
        JobPalette palette = jobPaletteRepository.findById(id)
                .filter(p -> !Boolean.TRUE.equals(p.getDeleted()))
                .orElseThrow(() -> new IllegalArgumentException("Palette not found: " + id));

        if (palette.getState() != JobPalette.PaletteState.VALIDATED) {
            throw new IllegalStateException("Palette must be VALIDATED to print");
        }

        // Mock PDF Generation
        String mockPath = "/storage/palettes/" + palette.getPaletteNumber() + ".pdf";
        palette.setPdfPath(mockPath);
        palette.setPrintedAt(LocalDateTime.now());
        palette.setState(JobPalette.PaletteState.PRINTED);
        palette.setUpdatedAt(LocalDateTime.now());

        JobPalette saved = jobPaletteRepository.save(palette);

        // Update all jobs to IN_PROGRESS
        for (JobPalette.JobInfo jobInfo : palette.getJobs()) {
            Job job = jobRepository.findById(jobInfo.getJobId()).orElseThrow();
            if (job.getState() == Job.JobState.NOT_STARTED) {
                job.setState(Job.JobState.IN_PROGRESS);
                job.setUpdatedAt(LocalDateTime.now());

                // Also start the first step if not started
                if (job.getStepTracking() != null && !job.getStepTracking().isEmpty()) {
                    Job.StepTracking firstStep = job.getStepTracking().get(0);
                    if (firstStep.getState() == Job.StepState.NOT_STARTED) {
                        firstStep.setState(Job.StepState.IN_PROGRESS);
                        firstStep.setStartDate(LocalDateTime.now());
                        auditLogService.log(job.getId(), "JOB", "START_STEP",
                                "Auto-started step " + firstStep.getStep() + " via Palette Print", "system");
                    }
                }

                jobRepository.save(job);
                auditLogService.log(job.getId(), "JOB", "START_JOB", "Job auto-started via Palette Print", "system");
            }
        }

        auditLogService.log(saved.getId(), "PALETTE", "PRINT_PALETTE", "Palette printed (PDF generated)", "system");
        return saved;
    }

    @Transactional
    public JobPalette archivePalette(String id) {
        JobPalette palette = jobPaletteRepository.findById(id)
                .filter(p -> !Boolean.TRUE.equals(p.getDeleted()))
                .orElseThrow(() -> new IllegalArgumentException("Palette not found: " + id));

        if (palette.getState() != JobPalette.PaletteState.PRINTED) {
            throw new IllegalStateException("Palette must be PRINTED to archive");
        }

        palette.setState(JobPalette.PaletteState.ARCHIVED);
        palette.setUpdatedAt(LocalDateTime.now());

        JobPalette saved = jobPaletteRepository.save(palette);
        auditLogService.log(saved.getId(), "PALETTE", "ARCHIVE_PALETTE", "Palette archived", "system");
        return saved;
    }

    public Optional<JobPalette> getPaletteById(String id) {
        return jobPaletteRepository.findById(id).filter(p -> !Boolean.TRUE.equals(p.getDeleted()));
    }
}
