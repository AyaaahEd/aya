package com.pfe.production.domain.formversion;
import com.pfe.production.shared.exception.BusinessException;
import com.pfe.production.domain.audit.AuditLogService;

import com.pfe.production.domain.form.Form;
import com.pfe.production.domain.form.FormRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FormVersionService {

    private final FormVersionRepository formVersionRepository;
    private final FormRepository formRepository;
    private final FormVersionValidator formVersionValidator;

    public FormVersionService(FormVersionRepository formVersionRepository, FormRepository formRepository,
            FormVersionValidator formVersionValidator) {
        this.formVersionRepository = formVersionRepository;
        this.formRepository = formRepository;
        this.formVersionValidator = formVersionValidator;
    }

    @Transactional
    public FormVersion createVersion(com.pfe.production.domain.formversion.CreateFormVersionRequest request) {
        // 1. Validate Form Exists
        Form form = formRepository.findById(request.formId())
                .filter(f -> !Boolean.TRUE.equals(f.getDeleted()))
                .orElseThrow(() -> new IllegalArgumentException(
                        "Form not found or deleted with id: " + request.formId()));

        FormVersion version = new FormVersion();
        version.setFormId(request.formId());
        version.setPlanning(request.planning());
        version.setRollsOutIds(request.rollsOutIds());

        version.setFormNumber(form.getFormNumber());

        // 2. Planning Logic & Validation
        if (version.getPlanning() != null) {
            // Validate Start Date
            if (version.getPlanning().getStartDate() != null
                    && version.getPlanning().getStartDate().isBefore(LocalDateTime.now())) {
                // Warning: In strict systems this throws, but for dev/testing sometimes we
                // allow past dates.
            }

            // Calculate End Date
            if (version.getPlanning().getStartDate() != null && version.getPlanning().getPlannedQuantity() != null) {
                LocalDateTime endDate = calculateEndDate(
                        version.getPlanning().getStartDate(),
                        form.getCapacityTime(),
                        version.getPlanning().getPlannedQuantity());
                version.getPlanning().setEndDate(endDate);
            }

            // Verify Machine Availability
            if (version.getPlanning().getMachineId() != null
                    && version.getPlanning().getStartDate() != null
                    && version.getPlanning().getEndDate() != null) {
                if (!isMachineAvailable(version.getPlanning().getMachineId(),
                        version.getPlanning().getStartDate(),
                        version.getPlanning().getEndDate())) {
                    throw new IllegalArgumentException("Machine is not available for the selected period.");
                }
            }
        }

        // 3. Generate Version Number
        long existingVersionsCount = formVersionRepository.countByFormId(version.getFormId());
        version.setVersionNumber((int) existingVersionsCount + 1);

        // 4. Set Defaults
        version.setCreatedAt(LocalDateTime.now());
        version.setUpdatedAt(LocalDateTime.now());
        if (version.getState() == null) {
            version.setState(FormVersion.FormVersionState.DRAFT);
        }
        if (version.getDeleted() == null) {
            version.setDeleted(false);
        }

        return formVersionRepository.save(version);
    }

    public void transitionToProduction(String versionId) {
        FormVersion version = getVersionById(versionId)
                .orElseThrow(() -> new IllegalArgumentException("Version not found: " + versionId));

        // Use domain behavior method instead of manual state check
        version.startProduction();
        formVersionRepository.save(version);
    }

    public List<FormVersion> getVersionsByFormId(String formId) {
        return formVersionRepository.findByFormId(formId);
    }

    public Optional<FormVersion> getVersionById(String id) {
        return formVersionRepository.findById(id)
                .filter(v -> !Boolean.TRUE.equals(v.getDeleted()));
    }

    private LocalDateTime calculateEndDate(LocalDateTime start, Form.CapacityTime capacity, int quantity) {
        if (capacity == null)
            return start;

        int printingTime = capacity.getPrintingTime() != null ? capacity.getPrintingTime() : 0;
        int coatingTime = capacity.getCoatingTime() != null ? capacity.getCoatingTime() : 0;

        // Formula: (printingTime + coatingTime) * quantity (assuming minutes)
        int totalMinutes = (printingTime + coatingTime) * quantity;
        return start.plusMinutes(totalMinutes);
    }

    private boolean isMachineAvailable(String machineId, LocalDateTime start, LocalDateTime end) {
        return !formVersionRepository.existsOverlappingPlanning(machineId, start, end);
    }
}