package com.pfe.production.domain.formversion;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record CreateFormVersionRequest(
        @NotNull(message = "Form ID is required") String formId,

        @Valid FormVersion.Planning planning,

        List<String> rollsOutIds) {
}