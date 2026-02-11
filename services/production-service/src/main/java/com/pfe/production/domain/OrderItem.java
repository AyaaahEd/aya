package com.pfe.production.domain;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "production_order_items")
public class OrderItem {

    @Id
    private String id;

    @Indexed(unique = true)
    private String itemId; // Order Item Number

    private String orderNumber;
    private FileMeta thumbnail;
    private List<Step> steps;
    private List<String> formIds;
    private OrderItemState state;
    private Boolean reprint;
    private ReprintState reprintState;
    private LocalDateTime promiseAvailableDate;
    private List<ConfigurationProperty> configurationProperties;
    private Boolean closed = false;
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

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public FileMeta getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(FileMeta thumbnail) {
        this.thumbnail = thumbnail;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public List<String> getFormIds() {
        return formIds;
    }

    public void setFormIds(List<String> formIds) {
        this.formIds = formIds;
    }

    public OrderItemState getState() {
        return state;
    }

    public void setState(OrderItemState state) {
        this.state = state;
    }

    public Boolean getReprint() {
        return reprint;
    }

    public void setReprint(Boolean reprint) {
        this.reprint = reprint;
    }

    public ReprintState getReprintState() {
        return reprintState;
    }

    public void setReprintState(ReprintState reprintState) {
        this.reprintState = reprintState;
    }

    public LocalDateTime getPromiseAvailableDate() {
        return promiseAvailableDate;
    }

    public void setPromiseAvailableDate(LocalDateTime promiseAvailableDate) {
        this.promiseAvailableDate = promiseAvailableDate;
    }

    public List<ConfigurationProperty> getConfigurationProperties() {
        return configurationProperties;
    }

    public void setConfigurationProperties(List<ConfigurationProperty> configurationProperties) {
        this.configurationProperties = configurationProperties;
    }

    public Boolean getClosed() {
        return closed;
    }

    public void setClosed(Boolean closed) {
        this.closed = closed;
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

    // Nested Classes and Enums

    public static class FileMeta {
        private String path;
        private String url;

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
    }

    public static class Step {
        private String name;
        private StepState state;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private Integer order;

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

        public Integer getOrder() {
            return order;
        }

        public void setOrder(Integer order) {
            this.order = order;
        }
    }

    public static class ReprintState {
        private Boolean requested;
        private Boolean pushed;
        private LocalDateTime requestedAt;
        private String reason;

        public Boolean getRequested() {
            return requested;
        }

        public void setRequested(Boolean requested) {
            this.requested = requested;
        }

        public Boolean getPushed() {
            return pushed;
        }

        public void setPushed(Boolean pushed) {
            this.pushed = pushed;
        }

        public LocalDateTime getRequestedAt() {
            return requestedAt;
        }

        public void setRequestedAt(LocalDateTime requestedAt) {
            this.requestedAt = requestedAt;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }
    }

    public static class ConfigurationProperty {
        private String type;
        private String code;
        private String label;

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

    public enum OrderItemState {
        PENDING,
        IN_PROGRESS,
        COMPLETED,
        SHIPPED
    }

    public enum StepState {
        PENDING,
        IN_PROGRESS,
        COMPLETED,
        SKIPPED
    }
}
