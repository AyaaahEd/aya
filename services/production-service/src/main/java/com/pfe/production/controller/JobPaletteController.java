package com.pfe.production.controller;

import com.pfe.production.domain.JobPalette;
import com.pfe.production.dto.CreatePaletteRequest;
import com.pfe.production.dto.PaletteResponse;
import com.pfe.production.service.JobPaletteService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/production/palettes")
public class JobPaletteController {

    private final JobPaletteService jobPaletteService;

    public JobPaletteController(JobPaletteService jobPaletteService) {
        this.jobPaletteService = jobPaletteService;
    }

    @PostMapping
    public ResponseEntity<PaletteResponse> createPalette(@Valid @RequestBody CreatePaletteRequest request) {
        try {
            JobPalette palette = jobPaletteService.createPalette(request);
            return ResponseEntity.ok(PaletteResponse.fromEntity(palette));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}/validate")
    public ResponseEntity<PaletteResponse> validatePalette(@PathVariable String id) {
        try {
            return ResponseEntity.ok(PaletteResponse.fromEntity(jobPaletteService.validatePalette(id)));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}/print")
    public ResponseEntity<PaletteResponse> printPalette(@PathVariable String id) {
        try {
            return ResponseEntity.ok(PaletteResponse.fromEntity(jobPaletteService.generatePdf(id)));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}/archive")
    public ResponseEntity<PaletteResponse> archivePalette(@PathVariable String id) {
        try {
            return ResponseEntity.ok(PaletteResponse.fromEntity(jobPaletteService.archivePalette(id)));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaletteResponse> getPaletteById(@PathVariable String id) {
        return jobPaletteService.getPaletteById(id)
                .map(PaletteResponse::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
