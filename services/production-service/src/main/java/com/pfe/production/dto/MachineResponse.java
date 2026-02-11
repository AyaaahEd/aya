package com.pfe.production.dto;

import com.pfe.production.domain.Machine;
import java.time.LocalDateTime;
import java.util.List;

public record MachineResponse(
        String id,
        String name,
        Machine.MachineStatus status,
        String location,
        Machine.MachineProcessing processing,
        Machine.Setup setup,
        List<Machine.Speed> speeds,
        Machine.WorkTimes workTimes,
        List<Machine.Reservation> reservations,
        String createdBy,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
    public static MachineResponse fromEntity(Machine machine) {
        return new MachineResponse(
                machine.getId(),
                machine.getName(),
                machine.getStatus(),
                machine.getLocation(),
                machine.getProcessing(),
                machine.getSetup(),
                machine.getSpeeds(),
                machine.getWorkTimes(),
                machine.getReservations(),
                machine.getCreatedBy(),
                machine.getCreatedAt(),
                machine.getUpdatedAt());
    }
}
