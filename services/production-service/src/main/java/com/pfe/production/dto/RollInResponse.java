package com.pfe.production.dto;

import com.pfe.production.domain.RollIn;
import java.time.LocalDateTime;

public record RollInResponse(
        String id,
        String rollNumber,
        String supplierCode,
        String qualityCode,
        Double width,
        Double length,
        RollIn.RollInStatus status,
        String createdBy,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
    public static RollInResponse fromEntity(RollIn roll) {
        return new RollInResponse(
                roll.getId(),
                roll.getRollNumber(),
                roll.getSupplierCode(),
                roll.getQualityCode(),
                roll.getWidth(),
                roll.getLength(),
                roll.getStatus(),
                roll.getCreatedBy(),
                roll.getCreatedAt(),
                roll.getUpdatedAt());
    }
}
