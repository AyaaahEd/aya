package com.pfe.production.domain.palette;

import java.time.LocalDateTime;
import java.util.List;

public record PaletteResponse(
        String id,
        String paletteNumber,
        JobPalette.PaletteState state,
        List<JobPalette.JobInfo> jobs,
        String machineId,
        String machineName,
        LocalDateTime printedAt,
        String pdfPath,
        Boolean deleted,
        String createdBy,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
    public static PaletteResponse fromEntity(JobPalette palette) {
        return new PaletteResponse(
                palette.getId(),
                palette.getPaletteNumber(),
                palette.getState(),
                palette.getJobs(),
                palette.getMachineId(),
                palette.getMachineName(),
                palette.getPrintedAt(),
                palette.getPdfPath(),
                palette.getDeleted(),
                palette.getCreatedBy(),
                palette.getCreatedAt(),
                palette.getUpdatedAt());
    }
}