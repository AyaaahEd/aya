package com.pfe.production.domain.formversion;

import java.time.LocalDateTime;
import java.util.List;

public record FormVersionResponse(
        String id,
        String formId,
        String formNumber,
        Integer versionNumber,
        FormVersion.FormVersionState state,
        FormVersion.Planning planning,
        List<String> rollsOutIds,
        Boolean deleted,
        String createdBy,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
    public static FormVersionResponse fromEntity(FormVersion version) {
        return new FormVersionResponse(
                version.getId(),
                version.getFormId(),
                version.getFormNumber(),
                version.getVersionNumber(),
                version.getState(),
                version.getPlanning(),
                version.getRollsOutIds(),
                version.getDeleted(),
                version.getCreatedBy(),
                version.getCreatedAt(),
                version.getUpdatedAt());
    }
}