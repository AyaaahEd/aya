package com.pfe.production.domain.machine;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/production/machines")
public class MachineController {

    private final MachineService machineService;

    public MachineController(MachineService machineService) {
        this.machineService = machineService;
    }

    @PostMapping
    public ResponseEntity<MachineResponse> createMachine(@Valid @RequestBody CreateMachineRequest request) {
        try {
            Machine machine = machineService.createMachine(request);
            return ResponseEntity.ok(MachineResponse.fromEntity(machine));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}/schedule")
    public ResponseEntity<MachineResponse> updateSchedule(@PathVariable String id,
            @RequestBody Machine.WorkTimes workTimes) {
        try {
            return ResponseEntity.ok(MachineResponse.fromEntity(machineService.updateSchedule(id, workTimes)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{id}/reservations")
    public ResponseEntity<MachineResponse> addReservation(@PathVariable String id,
            @RequestBody Machine.Reservation reservation) {
        try {
            return ResponseEntity.ok(MachineResponse.fromEntity(machineService.addReservation(id, reservation)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<MachineResponse>> getAllMachines() {
        List<MachineResponse> machines = machineService.findAll().stream()
                .map(MachineResponse::fromEntity)
                .toList();
        return ResponseEntity.ok(machines);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MachineResponse> getMachineById(@PathVariable String id) {
        try {
            return ResponseEntity.ok(MachineResponse.fromEntity(machineService.getMachineById(id)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}