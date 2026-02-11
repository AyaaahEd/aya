package com.pfe.production.domain.rollin;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateRollInRequest(
        @NotEmpty(message = "Roll Number is required") String rollNumber,

        String supplierCode,

        @NotEmpty(message = "Quality Code is required") String qualityCode,

        @NotNull(message = "Width is required") @Positive(message = "Width must be positive") Double width,

        @NotNull(message = "Length is required") @Positive(message = "Length must be positive") Double length) {
}
