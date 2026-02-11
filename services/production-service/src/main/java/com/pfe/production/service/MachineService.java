package com.pfe.production.service;

import com.pfe.production.domain.Machine;
import com.pfe.production.dto.CreateMachineRequest;
import com.pfe.production.repository.MachineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class MachineService {

    private final MachineRepository machineRepository;
    private final AuditLogService auditLogService;

    public MachineService(MachineRepository machineRepository, AuditLogService auditLogService) {
        this.machineRepository = machineRepository;
        this.auditLogService = auditLogService;
    }

    @Transactional
    public Machine createMachine(CreateMachineRequest request) {
        if (machineRepository.findByName(request.name()).isPresent()) {
            throw new IllegalArgumentException("Machine with name " + request.name() + " already exists");
        }

        Machine machine = new Machine();
        machine.setName(request.name());
        machine.setLocation(request.location());
        machine.setProcessing(request.processing());
        machine.setSetup(request.setup());
        machine.setSpeeds(request.speeds() != null ? request.speeds() : Collections.emptyList());
        machine.setWorkTimes(request.workTimes());
        machine.setStatus(Machine.MachineStatus.OPERATIONAL);
        machine.setReservations(new ArrayList<>());
        machine.setCreatedBy("system");
        machine.setCreatedAt(LocalDateTime.now());
        machine.setUpdatedAt(LocalDateTime.now());

        Machine saved = machineRepository.save(machine);
        auditLogService.log(saved.getId(), "MACHINE", "CREATE", "Machine created: " + saved.getName(), "system");
        return saved;
    }

    @Transactional
    public Machine updateSchedule(String id, Machine.WorkTimes workTimes) {
        Machine machine = machineRepository.findById(id)
                .filter(m -> !Boolean.TRUE.equals(m.getDeleted()))
                .orElseThrow(() -> new IllegalArgumentException("Machine not found: " + id));

        machine.setWorkTimes(workTimes);
        machine.setUpdatedAt(LocalDateTime.now());

        auditLogService.log(id, "MACHINE", "UPDATE_SCHEDULE", "Work schedule updated", "system");
        return machineRepository.save(machine);
    }

    @Transactional
    public Machine addReservation(String id, Machine.Reservation reservation) {
        Machine machine = machineRepository.findById(id)
                .filter(m -> !Boolean.TRUE.equals(m.getDeleted()))
                .orElseThrow(() -> new IllegalArgumentException("Machine not found: " + id));

        if (reservation.getStartDate().isAfter(reservation.getEndDate())) {
            throw new IllegalArgumentException("Start date must be before end date");
        }

        // Simple overlap check
        boolean overlap = machine.getReservations().stream()
                .anyMatch(r -> (reservation.getStartDate().isBefore(r.getEndDate())
                        && reservation.getEndDate().isAfter(r.getStartDate())));

        if (overlap) {
            throw new IllegalArgumentException("Reservation overlaps with existing reservation");
        }

        if (reservation.getId() == null) {
            reservation.setId(UUID.randomUUID().toString());
        }
        if (reservation.getCreatedBy() == null) {
            reservation.setCreatedBy("system");
        }

        if (machine.getReservations() == null) {
            machine.setReservations(new ArrayList<>());
        }

        machine.getReservations().add(reservation);
        machine.setUpdatedAt(LocalDateTime.now());

        auditLogService.log(id, "MACHINE", "ADD_RESERVATION", "Reservation added: " + reservation.getReason(),
                "system");
        return machineRepository.save(machine);
    }

    public List<Machine> findAll() {
        return machineRepository.findAll();
    }

    public Machine getMachineById(String id) {
        return machineRepository.findById(id)
                .filter(m -> !Boolean.TRUE.equals(m.getDeleted()))
                .orElseThrow(() -> new IllegalArgumentException("Machine not found: " + id));
    }
}
