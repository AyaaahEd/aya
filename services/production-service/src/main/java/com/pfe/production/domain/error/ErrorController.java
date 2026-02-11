package com.pfe.production.domain.error;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/errors")
public class ErrorController {

    private final ErrorService errorService;

    public ErrorController(ErrorService errorService) {
        this.errorService = errorService;
    }

    @GetMapping
    public List<ErrorEntry> getAllErrors() {
        return errorService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ErrorEntry> getErrorById(@PathVariable String id) {
        return errorService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/resolve")
    public ResponseEntity<ErrorEntry> resolveError(@PathVariable String id,
            @RequestParam String resolution,
            @RequestParam String resolvedBy) {
        return ResponseEntity.ok(errorService.resolveError(id, resolution, resolvedBy));
    }
}