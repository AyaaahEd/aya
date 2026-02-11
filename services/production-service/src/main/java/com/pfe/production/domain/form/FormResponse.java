package com.pfe.production.domain.form;

import java.time.LocalDateTime;
import java.util.List;

public record FormResponse(
        String id,
        String formNumber,
        Form.FileMeta thumbnail,
        Integer repetition,
        Form.Size size,
        Form.FormState state,
        Boolean reprint,
        Form.CapacityTime capacityTime,
        List<Form.Step> steps,
        List<String> orderItemIds,
        List<Form.ConfigurationProperty> configurationProperties,
        Boolean deleted,
        String createdBy,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
    public static FormResponse fromEntity(Form form) {
        return new FormResponse(
                form.getId(),
                form.getFormNumber(),
                form.getThumbnail(),
                form.getRepetition(),
                form.getSize(),
                form.getState(),
                form.getReprint(),
                form.getCapacityTime(),
                form.getSteps(),
                form.getOrderItemIds(),
                form.getConfigurationProperties(),
                form.getDeleted(),
                form.getCreatedBy(),
                form.getCreatedAt(),
                form.getUpdatedAt());
    }
}