package com.pfe.production.domain.palette;

import com.pfe.production.domain.job.Job;
import com.pfe.production.domain.palette.JobPalette.PaletteState;
import com.pfe.production.shared.exception.BusinessException;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Domain validator for JobPalette aggregate.
 * Encapsulates business rules from Section 12.4 of the DDD spec.
 */
@Component
public class PaletteValidator {

    public static final int MAX_JOBS_PER_PALETTE = 10;

    /**
     * Validates all jobs are for the same machine (first printing step).
     */
    public void validateSameMachine(List<Job> jobs) {
        if (jobs == null || jobs.size() <= 1)
            return;

        String firstMachineId = getFirstStepMachineId(jobs.get(0));
        for (int i = 1; i < jobs.size(); i++) {
            String machineId = getFirstStepMachineId(jobs.get(i));
            if (firstMachineId != null && !firstMachineId.equals(machineId)) {
                throw new BusinessException(
                        "All jobs in a palette must be for the same machine. Job " +
                                jobs.get(i).getJobNumber() + " has different machine.");
            }
        }
    }

    /**
     * Validates max 10 jobs per palette.
     */
    public void validateMaxJobs(int currentCount) {
        if (currentCount > MAX_JOBS_PER_PALETTE) {
            throw new BusinessException(
                    "Maximum " + MAX_JOBS_PER_PALETTE + " jobs per palette. Current count: " + currentCount);
        }
    }

    /**
     * Validates jobs are not already assigned to another palette.
     */
    public void validateJobsNotAssigned(List<Job> jobs) {
        if (jobs == null)
            return;
        for (Job job : jobs) {
            if (job.getJobPaletteId() != null) {
                throw new BusinessException(
                        "Job " + job.getJobNumber() + " is already assigned to palette: " + job.getJobPaletteId());
            }
        }
    }

    /**
     * Only VALIDATED palettes can be printed.
     */
    public void validateCanPrint(JobPalette palette) {
        if (palette.getState() != PaletteState.VALIDATED) {
            throw new BusinessException(
                    "Only VALIDATED palettes can be printed. Current state: " + palette.getState());
        }
    }

    /**
     * Only PRINTED palettes can be archived.
     */
    public void validateCanArchive(JobPalette palette) {
        if (palette.getState() != PaletteState.PRINTED) {
            throw new BusinessException(
                    "Only PRINTED palettes can be archived. Current state: " + palette.getState());
        }
    }

    /**
     * Validates a state transition is allowed.
     * CREATED → VALIDATED → PRINTED → ARCHIVED
     */
    public void validateStateTransition(PaletteState from, PaletteState to) {
        boolean valid = switch (from) {
            case CREATED -> to == PaletteState.VALIDATED;
            case VALIDATED -> to == PaletteState.PRINTED;
            case PRINTED -> to == PaletteState.ARCHIVED;
            case ARCHIVED -> false;
        };

        if (!valid) {
            throw new BusinessException(
                    "Invalid palette state transition: " + from + " → " + to);
        }
    }

    private String getFirstStepMachineId(Job job) {
        if (job.getStepTracking() != null && !job.getStepTracking().isEmpty()) {
            return job.getStepTracking().get(0).getMachineId();
        }
        return null;
    }
}