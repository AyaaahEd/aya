package com.pfe.production.domain.error;

import java.time.LocalDateTime;

public record ErrorResponse(
        String id,
        String entityType,
        String entityId,
        String errorCode,
        String errorMessage,
        String stackTrace,
        ErrorEntry.ErrorStatus status,
        String resolvedBy,
        LocalDateTime resolvedAt,
        String resolution,
        LocalDateTime createdAt) {
    public static ErrorResponse fromEntity(ErrorEntry error) {
        return new ErrorResponse(
                error.getId(),
                error.getEntityType(),
                error.getEntityId(),
                error.getErrorCode(),
                error.getErrorMessage(),
                error.getStackTrace(),
                error.getStatus(),
                error.getResolvedBy(),
                error.getResolvedAt(),
                error.getResolution(),
                error.getCreatedAt());
    }
}