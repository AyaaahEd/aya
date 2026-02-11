package com.pfe.production.domain.formversion;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/production/form-versions")
public class FormVersionController {

    private final FormVersionService formVersionService;

    public FormVersionController(FormVersionService formVersionService) {
        this.formVersionService = formVersionService;
    }

    @PostMapping
    public ResponseEntity<FormVersionResponse> createVersion(@Valid @RequestBody CreateFormVersionRequest request) {
        try {
            FormVersion version = formVersionService.createVersion(request);
            return ResponseEntity.ok(FormVersionResponse.fromEntity(version));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/form/{formId}")
    public ResponseEntity<List<FormVersionResponse>> getVersionsByFormId(@PathVariable String formId) {
        List<FormVersionResponse> versions = formVersionService.getVersionsByFormId(formId).stream()
                .map(FormVersionResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(versions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FormVersionResponse> getVersionById(@PathVariable String id) {
        return formVersionService.getVersionById(id)
                .map(FormVersionResponse::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/transition")
    public ResponseEntity<Void> transitionToProduction(@PathVariable String id) {
        try {
            formVersionService.transitionToProduction(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}