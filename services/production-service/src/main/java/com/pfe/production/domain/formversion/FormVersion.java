package com.pfe.production.domain.formversion;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "form_versions")
public class FormVersion {

    @Id
    private String id;
    private String formId;
    private String formNumber;
    private Integer versionNumber;
    private FormVersionState state;
    private Planning planning;
    private List<String> rollsOutIds;
    private Boolean deleted = false;
    private String createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getFormNumber() {
        return formNumber;
    }

    public void setFormNumber(String formNumber) {
        this.formNumber = formNumber;
    }

    public Integer getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(Integer versionNumber) {
        this.versionNumber = versionNumber;
    }

    public FormVersionState getState() {
        return state;
    }

    public void setState(FormVersionState state) {
        this.state = state;
    }

    public Planning getPlanning() {
        return planning;
    }

    public void setPlanning(Planning planning) {
        this.planning = planning;
    }

    public List<String> getRollsOutIds() {
        return rollsOutIds;
    }

    public void setRollsOutIds(List<String> rollsOutIds) {
        this.rollsOutIds = rollsOutIds;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // ===== Domain Behavior Methods =====

    /**
     * Validates this version (DRAFT → VALIDATED).
     * 
     * @throws IllegalStateException if not in DRAFT state
     */
    public void validate() {
        if (this.state != FormVersionState.DRAFT) {
            throw new IllegalStateException(
                    "Only DRAFT versions can be validated. Current state: " + state);
        }
        this.state = FormVersionState.VALIDATED;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Starts production for this version (VALIDATED → IN_PRODUCTION).
     * 
     * @throws IllegalStateException if not in VALIDATED state
     */
    public void startProduction() {
        if (this.state != FormVersionState.VALIDATED) {
            throw new IllegalStateException(
                    "Only VALIDATED versions can start production. Current state: " + state);
        }
        this.state = FormVersionState.IN_PRODUCTION;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Completes this version (IN_PRODUCTION → COMPLETED).
     * 
     * @throws IllegalStateException if not in IN_PRODUCTION state
     */
    public void complete() {
        if (this.state != FormVersionState.IN_PRODUCTION) {
            throw new IllegalStateException(
                    "Only IN_PRODUCTION versions can be completed. Current state: " + state);
        }
        this.state = FormVersionState.COMPLETED;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Checks if this version's planning overlaps with a given time range.
     */
    public boolean isPlanningOverlapping(LocalDateTime otherStart, LocalDateTime otherEnd) {
        if (planning == null || planning.getStartDate() == null || planning.getEndDate() == null) {
            return false;
        }
        return planning.getStartDate().isBefore(otherEnd) && planning.getEndDate().isAfter(otherStart);
    }

    // Nested Classes
    public static class Planning {
        private String machineId;
        private String machineName;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private Integer plannedQuantity;
        private Integer actualQuantity;
        private String status;

        public String getMachineId() {
            return machineId;
        }

        public void setMachineId(String machineId) {
            this.machineId = machineId;
        }

        public String getMachineName() {
            return machineName;
        }

        public void setMachineName(String machineName) {
            this.machineName = machineName;
        }

        public LocalDateTime getStartDate() {
            return startDate;
        }

        public void setStartDate(LocalDateTime startDate) {
            this.startDate = startDate;
        }

        public LocalDateTime getEndDate() {
            return endDate;
        }

        public void setEndDate(LocalDateTime endDate) {
            this.endDate = endDate;
        }

        public Integer getPlannedQuantity() {
            return plannedQuantity;
        }

        public void setPlannedQuantity(Integer plannedQuantity) {
            this.plannedQuantity = plannedQuantity;
        }

        public Integer getActualQuantity() {
            return actualQuantity;
        }

        public void setActualQuantity(Integer actualQuantity) {
            this.actualQuantity = actualQuantity;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

    public enum FormVersionState {
        DRAFT,
        VALIDATED,
        IN_PRODUCTION,
        COMPLETED,
        CANCELLED
    }
}
