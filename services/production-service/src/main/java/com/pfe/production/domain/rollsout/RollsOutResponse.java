package com.pfe.production.domain.rollsout;

import java.time.LocalDateTime;

public record RollsOutResponse(
        String id,
        String rollsOutNumber,
        String formVersionId,
        String formNumber,
        RollsOut.RollsOutState state,
        String pdfPath,
        String createdBy,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
    public static RollsOutResponse fromEntity(RollsOut roll) {
        return new RollsOutResponse(
                roll.getId(),
                roll.getRollsOutNumber(),
                roll.getFormVersionId(),
                roll.getFormNumber(),
                roll.getState(),
                roll.getPdfPath(),
                roll.getCreatedBy(),
                roll.getCreatedAt(),
                roll.getUpdatedAt());
    }
}