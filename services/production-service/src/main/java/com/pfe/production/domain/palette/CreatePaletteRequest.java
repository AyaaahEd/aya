package com.pfe.production.domain.palette;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreatePaletteRequest(
        @NotEmpty(message = "Job IDs are required") @Size(max = 10, message = "Maximum 10 jobs allowed per palette") List<String> jobIds,

        @NotNull(message = "Machine ID is required") String machineId) {
}
