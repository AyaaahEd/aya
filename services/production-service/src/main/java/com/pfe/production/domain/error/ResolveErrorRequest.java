package com.pfe.production.domain.error;

import jakarta.validation.constraints.NotEmpty;

public record ResolveErrorRequest(
        @NotEmpty(message = "Resolution note is required") String resolution,

        @NotEmpty(message = "User ID is required") String userId) {
}
