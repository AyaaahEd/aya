package com.pfe.production.service;

import com.pfe.production.domain.RollsOut;
import com.pfe.production.domain.RollsOut.RollsOutState;
import com.pfe.production.dto.CreateRollsOutRequest;
import com.pfe.production.repository.RollsOutRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pfe.production.repository.JobRepository;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RollsOutService {

    private final RollsOutRepository rollsOutRepository;
    private final AuditLogService auditLogService;
    private final JobRepository jobRepository;
    private final JobService jobService;

    public RollsOutService(RollsOutRepository rollsOutRepository, AuditLogService auditLogService,
            JobRepository jobRepository, JobService jobService) {
        this.rollsOutRepository = rollsOutRepository;
        this.auditLogService = auditLogService;
        this.jobRepository = jobRepository;
        this.jobService = jobService;
    }

    @Transactional
    public RollsOut createRollsOut(CreateRollsOutRequest request) {
        if (rollsOutRepository.findByRollsOutNumber(request.rollsOutNumber()).isPresent()) {
            throw new IllegalArgumentException("RollsOut with number " + request.rollsOutNumber() + " already exists");
        }

        RollsOut roll = new RollsOut();
        roll.setRollsOutNumber(request.rollsOutNumber());
        roll.setFormVersionId(request.formVersionId());
        roll.setFormNumber(request.formNumber());
        roll.setState(RollsOutState.PENDING); // Set initial state to PENDING
        roll.setPdfPath(request.pdfPath());
        roll.setCreatedBy("system"); // Should be current user
        roll.setCreatedAt(LocalDateTime.now());
        roll.setUpdatedAt(LocalDateTime.now());

        RollsOut saved = rollsOutRepository.save(roll);
        auditLogService.log(saved.getId(), "ROLLS_OUT", "CREATE", "Output Roll created: " + saved.getRollsOutNumber(),
                "system");
        return saved;
    }

    @Transactional
    public RollsOut updateState(String id, RollsOutState newState) {
        RollsOut roll = rollsOutRepository.findById(id)
                .filter(r -> !Boolean.TRUE.equals(r.getDeleted()))
                .orElseThrow(() -> new IllegalArgumentException("RollsOut not found: " + id));

        RollsOutState oldState = roll.getState();
        roll.setState(newState);
        roll.setUpdatedAt(LocalDateTime.now());

        RollsOut saved = rollsOutRepository.save(roll);
        auditLogService.log(saved.getId(), "ROLLS_OUT", "UPDATE_STATE",
                "State changed from " + oldState + " to " + newState, "system");

        // Trigger Job Progression if FINISHED
        if (newState == RollsOutState.FINISHED) {
            List<com.pfe.production.domain.Job> jobs = jobRepository.findByFormVersionId(roll.getFormVersionId());
            for (com.pfe.production.domain.Job job : jobs) {
                // Logic: If job is waiting for printing completion (which we assume is the
                // first step or current step)
                // We can force complete the "PRINTING" step if it exists and is IN_PROGRESS
                // Or we can just log/notify.
                // The requirement: "update Jobs associated so they pass to the next step in the
                // queue of cutting machines".
                // This implies completing the PRINTING step.
                try {
                    // Try to complete Printing step if active
                    jobService.completeStep(job.getId(), "PRINTING", job.getQuantity(),
                            "Roll Finished: " + roll.getRollsOutNumber());
                    // And start next step (Cutting)
                    jobService.startNextStep(job.getId(), null); // Machine allocation for next step might be needed or
                                                                 // handled later
                } catch (Exception e) {
                    // Log error but don't fail transaction? Or fail?
                    // Let's log for now.
                    System.err.println("Failed to auto-advance job " + job.getJobNumber() + ": " + e.getMessage());
                }
            }
        }

        return saved;
    }

    public List<RollsOut> findByFormVersionId(String formVersionId) {
        return rollsOutRepository.findByFormVersionId(formVersionId);
    }

    public RollsOut getRollsOutById(String id) {
        return rollsOutRepository.findById(id)
                .filter(r -> !Boolean.TRUE.equals(r.getDeleted()))
                .orElseThrow(() -> new IllegalArgumentException("RollsOut not found: " + id));
    }
}
