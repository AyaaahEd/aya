package com.pfe.production.domain.job;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "production_jobs")
public class Job {

    @Id
    private String id;

    @Indexed(unique = true)
    private String jobNumber;

    private String orderItemId;
    private String orderItemNumber;
    private String formVersionId;
    private String formNumber;
    private Integer quantity;
    private JobState state;
    private Boolean reprint;
    private String groupedId;
    private List<StepTracking> stepTracking;
    private String jobPaletteId;
    private Boolean deleted = false;

    @CreatedBy
    private String createdBy;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    // Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    public String getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(String orderItemId) {
        this.orderItemId = orderItemId;
    }

    public String getOrderItemNumber() {
        return orderItemNumber;
    }

    public void setOrderItemNumber(String orderItemNumber) {
        this.orderItemNumber = orderItemNumber;
    }

    public String getFormVersionId() {
        return formVersionId;
    }

    public void setFormVersionId(String formVersionId) {
        this.formVersionId = formVersionId;
    }

    public String getFormNumber() {
        return formNumber;
    }

    public void setFormNumber(String formNumber) {
        this.formNumber = formNumber;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public JobState getState() {
        return state;
    }

    public void setState(JobState state) {
        this.state = state;
    }

    public Boolean getReprint() {
        return reprint;
    }

    public void setReprint(Boolean reprint) {
        this.reprint = reprint;
    }

    public String getGroupedId() {
        return groupedId;
    }

    public void setGroupedId(String groupedId) {
        this.groupedId = groupedId;
    }

    public List<StepTracking> getStepTracking() {
        return stepTracking;
    }

    public void setStepTracking(List<StepTracking> stepTracking) {
        this.stepTracking = stepTracking;
    }

    public String getJobPaletteId() {
        return jobPaletteId;
    }

    public void setJobPaletteId(String jobPaletteId) {
        this.jobPaletteId = jobPaletteId;
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
     * Starts this job: transitions NOT_STARTED → IN_PROGRESS and starts the first
     * step.
     * 
     * @throws IllegalStateException if not in NOT_STARTED state
     */
    public void start() {
        if (this.state != JobState.NOT_STARTED) {
            throw new IllegalStateException("Job can only be started if in NOT_STARTED state. Current: " + state);
        }
        this.state = JobState.IN_PROGRESS;
        if (stepTracking != null && !stepTracking.isEmpty()) {
            StepTracking first = stepTracking.get(0);
            first.setState(StepState.IN_PROGRESS);
            first.setStartDate(LocalDateTime.now());
        }
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Completes the currently active step and starts the next one if available.
     * If all steps are complete, transitions the job to COMPLETED.
     */
    public void completeCurrentStep(Integer actualQuantity, String notes) {
        if (stepTracking == null || stepTracking.isEmpty()) {
            throw new IllegalStateException("Job has no steps to complete");
        }

        StepTracking activeStep = stepTracking.stream()
                .filter(st -> st.getState() == StepState.IN_PROGRESS)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No active step found to complete"));

        activeStep.setState(StepState.COMPLETED);
        activeStep.setEndDate(LocalDateTime.now());
        activeStep.setActualQuantity(actualQuantity);
        activeStep.setNotes(notes);

        // Start next step if available
        int activeIndex = stepTracking.indexOf(activeStep);
        if (activeIndex + 1 < stepTracking.size()) {
            StepTracking nextStep = stepTracking.get(activeIndex + 1);
            nextStep.setState(StepState.IN_PROGRESS);
            nextStep.setStartDate(LocalDateTime.now());
        }

        // Check if all steps are complete
        if (isComplete()) {
            this.state = JobState.COMPLETED;
        }

        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Checks if all steps are COMPLETED.
     */
    public boolean isComplete() {
        return stepTracking != null && !stepTracking.isEmpty()
                && stepTracking.stream().allMatch(st -> st.getState() == StepState.COMPLETED);
    }

    /**
     * Checks if this job can be cancelled (not already COMPLETED).
     */
    public boolean canBeCancelled() {
        return this.state != JobState.COMPLETED;
    }

    // Nested Classes and Enums

    public static class StepTracking {
        private String step; // Enum name or String? User said "StepName"
        private String machineId;
        private String machineName;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private StepState state;
        private Integer actualQuantity;
        private String notes;

        // Getters and Setters
        public String getStep() {
            return step;
        }

        public void setStep(String step) {
            this.step = step;
        }

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

        public StepState getState() {
            return state;
        }

        public void setState(StepState state) {
            this.state = state;
        }

        public Integer getActualQuantity() {
            return actualQuantity;
        }

        public void setActualQuantity(Integer actualQuantity) {
            this.actualQuantity = actualQuantity;
        }

        public String getNotes() {
            return notes;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }
    }

    public enum JobState {
        NOT_STARTED,
        IN_PROGRESS,
        COMPLETED,
        FAILED,
        CANCELLED
    }

    public enum StepState {
        NOT_STARTED,
        IN_PROGRESS,
        COMPLETED,
        FAILED
    }
}
