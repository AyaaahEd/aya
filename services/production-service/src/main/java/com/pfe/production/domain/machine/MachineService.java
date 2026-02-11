package com.pfe.production.domain.machine;
import com.pfe.production.shared.exception.BusinessException;
import com.pfe.production.domain.audit.AuditLogService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class MachineService {

    private final MachineRepository machineRepository;
    private final AuditLogService auditLogService;
    private final MachineValidator machineValidator;

    public MachineService(MachineRepository machineRepository, AuditLogService auditLogService,
            MachineValidator machineValidator) {
        this.machineRepository = machineRepository;
        this.auditLogService = auditLogService;
        this.machineValidator = machineValidator;
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

        // Domain validation
        machineValidator.validateSetup(machine.getSetup());
        machineValidator.validateWorkTimes(machine.getWorkTimes());

        Machine saved = machineRepository.save(machine);
        auditLogService.log(saved.getId(), "MACHINE", "CREATE", "Machine created: " + saved.getName(), "system");
        return saved;
    }

    @Transactional
    public Machine updateSchedule(String id, Machine.WorkTimes workTimes) {
        Machine machine = machineRepository.findById(id)
                .filter(m -> !Boolean.TRUE.equals(m.getDeleted()))
                .orElseThrow(() -> new IllegalArgumentException("Machine not found: " + id));

        // Domain validation
        machineValidator.validateWorkTimes(workTimes);

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

        // Use domain validator
        machineValidator.validateNewReservation(reservation, machine.getReservations());

        // Use domain behavior method
        machine.addReservation(reservation);

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