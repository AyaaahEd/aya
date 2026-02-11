package com.pfe.production.domain.error;

import jakarta.validation.constraints.NotEmpty;

public record CreateErrorRequest(
        @NotEmpty(message = "Entity Type is required") String entityType,

        @NotEmpty(message = "Entity ID is required") String entityId,

        String errorCode,

        @NotEmpty(message = "Error Message is required") String errorMessage,

        String stackTrace) {
}
