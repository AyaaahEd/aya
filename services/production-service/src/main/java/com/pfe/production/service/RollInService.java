package com.pfe.production.service;

import com.pfe.production.domain.RollIn;
import com.pfe.production.domain.RollIn.RollInStatus; // Ensure correct import
import com.pfe.production.dto.CreateRollInRequest;
import com.pfe.production.repository.RollInRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import com.pfe.production.exception.BusinessException;

@Service
public class RollInService {

    private final RollInRepository rollInRepository;
    private final AuditLogService auditLogService;

    public RollInService(RollInRepository rollInRepository, AuditLogService auditLogService) {
        this.rollInRepository = rollInRepository;
        this.auditLogService = auditLogService;
    }

    @Transactional
    public RollIn createRollIn(CreateRollInRequest request) {
        if (rollInRepository.findByRollNumber(request.rollNumber()).isPresent()) {
            throw new IllegalArgumentException("Roll with number " + request.rollNumber() + " already exists");
        }

        RollIn roll = new RollIn();
        roll.setRollNumber(request.rollNumber());
        roll.setSupplierCode(request.supplierCode());
        roll.setQualityCode(request.qualityCode());
        roll.setWidth(request.width());
        roll.setLength(request.length());
        roll.setStatus(RollInStatus.IN_STOCK);
        roll.setCreatedBy("system");
        roll.setCreatedAt(LocalDateTime.now());
        roll.setUpdatedAt(LocalDateTime.now());

        RollIn saved = rollInRepository.save(roll);
        auditLogService.log(saved.getId(), "ROLL_IN", "CREATE", "Roll created: " + saved.getRollNumber(), "system");
        return saved;
    }

    /**
     * Finds the best compatible roll for allocation.
     * Criteria:
     * 1. Matches qualityCode
     * 2. Status is IN_STOCK
     * 3. Width >= Required Width (closest match preferred to minimize waste, or
     * just fit)
     * 4. Length >= Required Length (assuming we allocate whole roll or part? Spec
     * says "Allocate Roll", likely whole)
     */
    public Optional<RollIn> findCompatibleRoll(String qualityCode, Double minWidth, Double minLength) {
        return rollInRepository.findFirstByQualityCodeAndStatusAndWidthGreaterThanEqualAndLengthGreaterThanEqual(
                qualityCode, RollInStatus.IN_STOCK, minWidth, minLength);
    }

    @Transactional
    public RollIn allocateRoll(String id) {
        RollIn roll = rollInRepository.findById(id)
                .filter(r -> !Boolean.TRUE.equals(r.getDeleted()))
                .orElseThrow(() -> new IllegalArgumentException("Roll not found: " + id));

        if (roll.getStatus() != RollInStatus.IN_STOCK) {
            throw new IllegalStateException("Roll is not in stock. Current status: " + roll.getStatus());
        }

        roll.setStatus(RollInStatus.ALLOCATED);
        roll.setUpdatedAt(LocalDateTime.now());

        auditLogService.log(id, "ROLL_IN", "ALLOCATE", "Roll allocated", "system");
        return rollInRepository.save(roll);
    }

    @Transactional
    public RollIn markAsUsed(String id) {
        RollIn roll = rollInRepository.findById(id)
                .filter(r -> !Boolean.TRUE.equals(r.getDeleted()))
                .orElseThrow(() -> new IllegalArgumentException("Roll not found: " + id));

        roll.setStatus(RollInStatus.USED);
        roll.setUpdatedAt(LocalDateTime.now());

        auditLogService.log(id, "ROLL_IN", "USE", "Roll marked as used", "system");
        return rollInRepository.save(roll);
    }

    public List<RollIn> findAll() {
        return rollInRepository.findAll();
    }

    public Optional<RollIn> getRollById(String id) {
        return rollInRepository.findById(id).filter(r -> !Boolean.TRUE.equals(r.getDeleted()));
    }

    @Transactional
    public RollIn allocateBestRoll(String qualityCode, Double minWidth, Double minLength) {
        return findCompatibleRoll(qualityCode, minWidth, minLength)
                .map(roll -> allocateRoll(roll.getId()))
                .orElseThrow(() -> new BusinessException(
                        "No compatible roll found for quality: " + qualityCode + ", width: " + minWidth + ", length: "
                                + minLength,
                        "INVENTORY_SHORTAGE",
                        "ROLL_IN",
                        "UNKNOWN"));
    }
}
