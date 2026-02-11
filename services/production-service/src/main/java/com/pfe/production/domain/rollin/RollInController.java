package com.pfe.production.domain.rollin;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/production/rolls")
public class RollInController {

    private final RollInService rollInService;

    public RollInController(RollInService rollInService) {
        this.rollInService = rollInService;
    }

    @PostMapping
    public ResponseEntity<RollInResponse> createRoll(@Valid @RequestBody CreateRollInRequest request) {
        try {
            RollIn roll = rollInService.createRollIn(request);
            return ResponseEntity.ok(RollInResponse.fromEntity(roll));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/available")
    public ResponseEntity<RollInResponse> findAvailableRoll(
            @RequestParam String quality,
            @RequestParam Double width,
            @RequestParam Double length) {
        return rollInService.findCompatibleRoll(quality, width, length)
                .map(RollInResponse::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    @PutMapping("/{id}/allocate")
    public ResponseEntity<RollInResponse> allocateRoll(@PathVariable String id) {
        try {
            return ResponseEntity.ok(RollInResponse.fromEntity(rollInService.allocateRoll(id)));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<RollInResponse>> getAllRolls() {
        List<RollInResponse> rolls = rollInService.findAll().stream()
                .map(RollInResponse::fromEntity)
                .toList();
        return ResponseEntity.ok(rolls);
    }
}