package com.pfe.production.dto;

import com.pfe.production.domain.Machine;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record CreateMachineRequest(
        @NotEmpty(message = "Name is required") String name,

        String location,

        @NotNull(message = "Processing type is required") Machine.MachineProcessing processing,

        Machine.Setup setup,

        List<Machine.Speed> speeds,

        Machine.WorkTimes workTimes) {
}
