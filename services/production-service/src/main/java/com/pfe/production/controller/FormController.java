package com.pfe.production.controller;

import com.pfe.production.domain.Form;
import com.pfe.production.dto.CreateFormRequest;
import com.pfe.production.dto.FormResponse;
import com.pfe.production.service.FormService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/production/forms")
public class FormController {

    private final FormService formService;

    public FormController(FormService formService) {
        this.formService = formService;
    }

    @PostMapping
    public ResponseEntity<FormResponse> createForm(@Valid @RequestBody CreateFormRequest request) {
        Form createdForm = formService.createForm(request);
        return ResponseEntity.ok(FormResponse.fromEntity(createdForm));
    }

    @GetMapping
    public ResponseEntity<List<FormResponse>> getAllForms() {
        List<FormResponse> forms = formService.getAllForms().stream()
                .map(FormResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(forms);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FormResponse> getFormById(@PathVariable String id) {
        return formService.getFormById(id)
                .map(FormResponse::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/number/{formNumber}")
    public ResponseEntity<FormResponse> getFormByNumber(@PathVariable String formNumber) {
        return formService.getFormByNumber(formNumber)
                .map(FormResponse::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<FormResponse> updateForm(@PathVariable String id, @RequestBody Form form) {
        try {
            // Note: Ideally Update should also use a DTO (UpdateFormRequest)
            return ResponseEntity.ok(FormResponse.fromEntity(formService.updateForm(id, form)));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteForm(@PathVariable String id) {
        formService.deleteForm(id);
        return ResponseEntity.noContent().build();
    }
}
