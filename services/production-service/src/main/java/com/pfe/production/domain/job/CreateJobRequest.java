package com.pfe.production.domain.job;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CreateJobRequest(
        @NotNull(message = "Order Item ID is required") String orderItemId,

        @NotNull(message = "Order Item Number is required") String orderItemNumber,

        @NotNull(message = "Form Version ID is required") String formVersionId,

        @NotNull(message = "Quantity is required") @Min(value = 1, message = "Quantity must be greater than 0") Integer quantity,

        Boolean reprint,
        String groupedId,
        String jobPaletteId) {
}
