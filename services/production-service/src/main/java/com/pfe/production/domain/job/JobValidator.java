package com.pfe.production.domain.job;

import com.pfe.production.domain.job.Job.JobState;
import com.pfe.production.domain.job.Job.StepState;
import com.pfe.production.shared.exception.BusinessException;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Domain validator for Job aggregate.
 * Encapsulates business rules from Section 12.3 of the DDD spec.
 */
@Component
public class JobValidator {

    /**
     * Validates a job can be started (must be in NOT_STARTED state).
     */
    public void validateCanStart(Job job) {
        if (job.getState() != JobState.NOT_STARTED) {
            throw new BusinessException(
                    "Job can only be started if in NOT_STARTED state. Current state: " + job.getState());
        }
    }

    /**
     * Validates quantity is > 0.
     */
    public void validateQuantity(Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new BusinessException("Job quantity must be greater than 0, got: " + quantity);
        }
    }

    /**
     * Validates steps must be completed in order.
     * The given stepName can only be started if all previous steps are COMPLETED.
     */
    public void validateStepSequence(List<Job.StepTracking> stepTrackings, String stepName) {
        if (stepTrackings == null || stepTrackings.isEmpty()) {
            throw new BusinessException("Job has no step tracking defined");
        }

        boolean foundStep = false;
        for (Job.StepTracking st : stepTrackings) {
            if (st.getStep().equals(stepName)) {
                foundStep = true;
                break;
            }
            // All previous steps must be COMPLETED
            if (st.getState() != StepState.COMPLETED) {
                throw new BusinessException(
                        "Step '" + stepName + "' cannot be started. Previous step '" +
                                st.getStep() + "' is in state: " + st.getState());
            }
        }

        if (!foundStep) {
            throw new BusinessException("Step '" + stepName + "' not found in job step tracking");
        }
    }

    /**
     * Completed jobs cannot be cancelled.
     */
    public void validateCanCancel(Job job) {
        if (job.getState() == JobState.COMPLETED) {
            throw new BusinessException("Cannot cancel a completed job: " + job.getJobNumber());
        }
    }

    /**
     * Job can only be in one palette.
     */
    public void validateNotInPalette(Job job) {
        if (job.getJobPaletteId() != null) {
            throw new BusinessException(
                    "Job " + job.getJobNumber() + " is already assigned to palette: " + job.getJobPaletteId());
        }
    }

    /**
     * Actual quantity must be <= planned quantity.
     */
    public void validateActualQuantity(Integer actual, Integer planned) {
        if (actual != null && planned != null && actual > planned) {
            throw new BusinessException(
                    "Actual quantity (" + actual + ") cannot exceed planned quantity (" + planned + ")");
        }
    }

    /**
     * Validates a job is not deleted.
     */
    public void validateNotDeleted(Job job) {
        if (Boolean.TRUE.equals(job.getDeleted())) {
            throw new BusinessException("Cannot modify a deleted job: " + job.getJobNumber());
        }
    }
}