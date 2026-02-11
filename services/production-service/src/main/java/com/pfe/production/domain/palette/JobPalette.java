package com.pfe.production.domain.palette;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "production_palettes")
public class JobPalette {

    @Id
    private String id;

    @Indexed(unique = true)
    private String paletteNumber; // P2026-XXXX

    private PaletteState state;
    private List<JobInfo> jobs;
    private String machineId;
    private String machineName;
    private LocalDateTime printedAt;
    private String pdfPath;
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

    public String getPaletteNumber() {
        return paletteNumber;
    }

    public void setPaletteNumber(String paletteNumber) {
        this.paletteNumber = paletteNumber;
    }

    public PaletteState getState() {
        return state;
    }

    public void setState(PaletteState state) {
        this.state = state;
    }

    public List<JobInfo> getJobs() {
        return jobs;
    }

    public void setJobs(List<JobInfo> jobs) {
        this.jobs = jobs;
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

    public LocalDateTime getPrintedAt() {
        return printedAt;
    }

    public void setPrintedAt(LocalDateTime printedAt) {
        this.printedAt = printedAt;
    }

    public String getPdfPath() {
        return pdfPath;
    }

    public void setPdfPath(String pdfPath) {
        this.pdfPath = pdfPath;
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

    public static final int MAX_JOBS = 10;

    /**
     * Adds a job to this palette. Validates max capacity.
     * 
     * @throws IllegalStateException if palette is full
     */
    public void addJob(JobInfo jobInfo) {
        if (jobs == null) {
            jobs = new java.util.ArrayList<>();
        }
        if (isFull()) {
            throw new IllegalStateException("Palette is full. Maximum " + MAX_JOBS + " jobs allowed.");
        }
        jobInfo.setPosition(jobs.size() + 1);
        jobs.add(jobInfo);
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Checks if this palette has reached max capacity.
     */
    public boolean isFull() {
        return jobs != null && jobs.size() >= MAX_JOBS;
    }

    /**
     * Checks if this palette can be printed (must be VALIDATED).
     */
    public boolean canBePrinted() {
        return state == PaletteState.VALIDATED;
    }

    /**
     * Prints this palette: transitions VALIDATED → PRINTED.
     */
    public void print(String pdfPath) {
        if (state != PaletteState.VALIDATED) {
            throw new IllegalStateException("Only VALIDATED palettes can be printed. Current: " + state);
        }
        this.state = PaletteState.PRINTED;
        this.pdfPath = pdfPath;
        this.printedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Nested Classes and Enums
    public static class JobInfo {
        private String jobId;
        private String jobNumber;
        private String formNumber;
        private Integer quantity;
        private Integer position; // 1-10

        // Getters and Setters
        public String getJobId() {
            return jobId;
        }

        public void setJobId(String jobId) {
            this.jobId = jobId;
        }

        public String getJobNumber() {
            return jobNumber;
        }

        public void setJobNumber(String jobNumber) {
            this.jobNumber = jobNumber;
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

        public Integer getPosition() {
            return position;
        }

        public void setPosition(Integer position) {
            this.position = position;
        }
    }

    public enum PaletteState {
        CREATED,
        VALIDATED,
        PRINTED,
        ARCHIVED
    }
}
