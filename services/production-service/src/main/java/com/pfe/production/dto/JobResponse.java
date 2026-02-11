package com.pfe.production.dto;

import com.pfe.production.domain.Job;
import java.time.LocalDateTime;
import java.util.List;

public record JobResponse(
        String id,
        String jobNumber,
        String orderItemId,
        String orderItemNumber,
        String formVersionId,
        String formNumber,
        Integer quantity,
        Job.JobState state,
        Boolean reprint,
        String groupedId,
        List<Job.StepTracking> stepTracking,
        String jobPaletteId,
        Boolean deleted,
        Double completionPercentage,
        String createdBy,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
    public static JobResponse fromEntity(Job job) {
        double percentage = 0.0;
        if (job.getStepTracking() != null && !job.getStepTracking().isEmpty()) {
            long completedCount = job.getStepTracking().stream()
                    .filter(s -> s.getState() == Job.StepState.COMPLETED)
                    .count();
            percentage = (double) completedCount / job.getStepTracking().size() * 100.0;
        }

        return new JobResponse(
                job.getId(),
                job.getJobNumber(),
                job.getOrderItemId(),
                job.getOrderItemNumber(),
                job.getFormVersionId(),
                job.getFormNumber(),
                job.getQuantity(),
                job.getState(),
                job.getReprint(),
                job.getGroupedId(),
                job.getStepTracking(),
                job.getJobPaletteId(),
                job.getDeleted(),
                percentage,
                job.getCreatedBy(),
                job.getCreatedAt(),
                job.getUpdatedAt());
    }
}
