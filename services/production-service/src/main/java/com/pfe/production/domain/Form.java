package com.pfe.production.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "production_forms")
public class Form {
    @Id
    private String id;

    @Indexed(unique = true)
    private String formNumber;

    private FileMeta thumbnail;
    private Integer repetition;
    private Size size;
    private FormState state;
    private Boolean reprint;
    private CapacityTime capacityTime;
    private List<Step> steps;
    private List<String> orderItemIds;
    private List<ConfigurationProperty> configurationProperties;
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

    public String getFormNumber() {
        return formNumber;
    }

    public void setFormNumber(String formNumber) {
        this.formNumber = formNumber;
    }

    public FileMeta getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(FileMeta thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Integer getRepetition() {
        return repetition;
    }

    public void setRepetition(Integer repetition) {
        this.repetition = repetition;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public FormState getState() {
        return state;
    }

    public void setState(FormState state) {
        this.state = state;
    }

    public Boolean getReprint() {
        return reprint;
    }

    public void setReprint(Boolean reprint) {
        this.reprint = reprint;
    }

    public CapacityTime getCapacityTime() {
        return capacityTime;
    }

    public void setCapacityTime(CapacityTime capacityTime) {
        this.capacityTime = capacityTime;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public List<String> getOrderItemIds() {
        return orderItemIds;
    }

    public void setOrderItemIds(List<String> orderItemIds) {
        this.orderItemIds = orderItemIds;
    }

    public List<ConfigurationProperty> getConfigurationProperties() {
        return configurationProperties;
    }

    public void setConfigurationProperties(List<ConfigurationProperty> configurationProperties) {
        this.configurationProperties = configurationProperties;
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

    public static class FileMeta {
        private String path;
        private String url;
        private String fileName;
        private Long fileSize;
        private String mimeType;

        // Getters and Setters
        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public Long getFileSize() {
            return fileSize;
        }

        public void setFileSize(Long fileSize) {
            this.fileSize = fileSize;
        }

        public String getMimeType() {
            return mimeType;
        }

        public void setMimeType(String mimeType) {
            this.mimeType = mimeType;
        }
    }

    public static class Size {
        private Double width;
        private Double height;
        private String unit;

        // Getters and Setters
        public Double getWidth() {
            return width;
        }

        public void setWidth(Double width) {
            this.width = width;
        }

        public Double getHeight() {
            return height;
        }

        public void setHeight(Double height) {
            this.height = height;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }
    }

    public static class CapacityTime {
        private Integer printingTime;
        private Integer cuttingTime;
        private Integer sewingTime;
        private Integer coatingTime;
        private String unit;

        // Getters and Setters
        public Integer getPrintingTime() {
            return printingTime;
        }

        public void setPrintingTime(Integer printingTime) {
            this.printingTime = printingTime;
        }

        public Integer getCuttingTime() {
            return cuttingTime;
        }

        public void setCuttingTime(Integer cuttingTime) {
            this.cuttingTime = cuttingTime;
        }

        public Integer getSewingTime() {
            return sewingTime;
        }

        public void setSewingTime(Integer sewingTime) {
            this.sewingTime = sewingTime;
        }

        public Integer getCoatingTime() {
            return coatingTime;
        }

        public void setCoatingTime(Integer coatingTime) {
            this.coatingTime = coatingTime;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }
    }

    public static class Step {
        private String name;
        private StepState state;
        private Integer order;

        // Getters and Setters
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public StepState getState() {
            return state;
        }

        public void setState(StepState state) {
            this.state = state;
        }

        public Integer getOrder() {
            return order;
        }

        public void setOrder(Integer order) {
            this.order = order;
        }
    }

    public static class ConfigurationProperty {
        private String type;
        private String code;
        private String label;

        // Getters and Setters
        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }
    }

    public enum FormState {
        DRAFT,
        PENDING,
        IN_PROGRESS,
        COMPLETED,
        CANCELLED
    }

    public enum StepState {
        PENDING,
        IN_PROGRESS,
        COMPLETED,
        SKIPPED
    }
}
