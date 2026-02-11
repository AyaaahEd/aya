package com.pfe.production.domain;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Document(collection = "production_machines")
public class Machine {

    @Id
    private String id;

    @Indexed(unique = true)
    private String name;

    private MachineStatus status;
    private String location;
    private MachineProcessing processing; // Printing, Cutting, Coating, Sewing
    private Setup setup;
    private List<Speed> speeds;
    private WorkTimes workTimes;
    private List<Reservation> reservations;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MachineStatus getStatus() {
        return status;
    }

    public void setStatus(MachineStatus status) {
        this.status = status;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public MachineProcessing getProcessing() {
        return processing;
    }

    public void setProcessing(MachineProcessing processing) {
        this.processing = processing;
    }

    public Setup getSetup() {
        return setup;
    }

    public void setSetup(Setup setup) {
        this.setup = setup;
    }

    public List<Speed> getSpeeds() {
        return speeds;
    }

    public void setSpeeds(List<Speed> speeds) {
        this.speeds = speeds;
    }

    public WorkTimes getWorkTimes() {
        return workTimes;
    }

    public void setWorkTimes(WorkTimes workTimes) {
        this.workTimes = workTimes;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
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

    // Nested Classes

    public static class Setup {
        private Integer setupTime;
        private String unit; // Minutes

        public Integer getSetupTime() {
            return setupTime;
        }

        public void setSetupTime(Integer setupTime) {
            this.setupTime = setupTime;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }
    }

    public static class Speed {
        private String qualityCode;
        private Double speed;
        private String unit; // m/h, units/h

        public String getQualityCode() {
            return qualityCode;
        }

        public void setQualityCode(String qualityCode) {
            this.qualityCode = qualityCode;
        }

        public Double getSpeed() {
            return speed;
        }

        public void setSpeed(Double speed) {
            this.speed = speed;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }
    }

    public static class WorkTimes {
        private List<RegularWorkTime> regular;
        private List<OtherWorkTime> other;

        public List<RegularWorkTime> getRegular() {
            return regular;
        }

        public void setRegular(List<RegularWorkTime> regular) {
            this.regular = regular;
        }

        public List<OtherWorkTime> getOther() {
            return other;
        }

        public void setOther(List<OtherWorkTime> other) {
            this.other = other;
        }
    }

    public static class RegularWorkTime {
        private String dayOfWeek; // MONDAY, TUESDAY...
        private LocalTime startTime;
        private LocalTime endTime;

        public String getDayOfWeek() {
            return dayOfWeek;
        }

        public void setDayOfWeek(String dayOfWeek) {
            this.dayOfWeek = dayOfWeek;
        }

        public LocalTime getStartTime() {
            return startTime;
        }

        public void setStartTime(LocalTime startTime) {
            this.startTime = startTime;
        }

        public LocalTime getEndTime() {
            return endTime;
        }

        public void setEndTime(LocalTime endTime) {
            this.endTime = endTime;
        }
    }

    public static class OtherWorkTime {
        private LocalDateTime date;
        private String type; // HOLIDAY, OVERTIME, MAINTENANCE
        private LocalTime startTime;
        private LocalTime endTime;

        public LocalDateTime getDate() {
            return date;
        }

        public void setDate(LocalDateTime date) {
            this.date = date;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public LocalTime getStartTime() {
            return startTime;
        }

        public void setStartTime(LocalTime startTime) {
            this.startTime = startTime;
        }

        public LocalTime getEndTime() {
            return endTime;
        }

        public void setEndTime(LocalTime endTime) {
            this.endTime = endTime;
        }
    }

    public static class Reservation {
        private String id; // UUID
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private String reason;
        private String createdBy;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public String getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(String createdBy) {
            this.createdBy = createdBy;
        }
    }

    public enum MachineStatus {
        OPERATIONAL,
        MAINTENANCE,
        OFFLINE
    }

    public enum MachineProcessing {
        PRINTING,
        CUTTING,
        COATING,
        SEWING,
        PACKAGING
    }
}
