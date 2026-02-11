package com.pfe.production.domain.rollsout;

import jakarta.validation.constraints.NotEmpty;

public record CreateRollsOutRequest(
        @NotEmpty(message = "RollsOut Number is required") String rollsOutNumber,

        @NotEmpty(message = "Form Version ID is required") String formVersionId,

        @NotEmpty(message = "Form Number is required") String formNumber,

        String pdfPath) {
}
