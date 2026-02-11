package com.pfe.production.controller;

import com.pfe.production.domain.RollsOut;
import com.pfe.production.dto.CreateRollsOutRequest;
import com.pfe.production.dto.RollsOutResponse;
import com.pfe.production.service.RollsOutService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/production/rolls-out")
public class RollsOutController {

    private final RollsOutService rollsOutService;

    public RollsOutController(RollsOutService rollsOutService) {
        this.rollsOutService = rollsOutService;
    }

    @PostMapping
    public ResponseEntity<RollsOutResponse> createRollsOut(@Valid @RequestBody CreateRollsOutRequest request) {
        try {
            RollsOut roll = rollsOutService.createRollsOut(request);
            return ResponseEntity.ok(RollsOutResponse.fromEntity(roll));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}/state")
    public ResponseEntity<RollsOutResponse> updateState(@PathVariable String id,
            @RequestBody Map<String, String> payload) {
        try {
            String stateStr = payload.get("state");
            RollsOut.RollsOutState newState = RollsOut.RollsOutState.valueOf(stateStr);
            return ResponseEntity.ok(RollsOutResponse.fromEntity(rollsOutService.updateState(id, newState)));
        } catch (IllegalArgumentException | NullPointerException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/form-version/{formVersionId}")
    public ResponseEntity<List<RollsOutResponse>> getByFormVersionId(@PathVariable String formVersionId) {
        List<RollsOutResponse> rolls = rollsOutService.findByFormVersionId(formVersionId).stream()
                .map(RollsOutResponse::fromEntity)
                .toList();
        return ResponseEntity.ok(rolls);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RollsOutResponse> getRollsOutById(@PathVariable String id) {
        try {
            return ResponseEntity.ok(RollsOutResponse.fromEntity(rollsOutService.getRollsOutById(id)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
