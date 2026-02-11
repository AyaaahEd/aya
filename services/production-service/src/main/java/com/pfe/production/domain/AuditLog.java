package com.pfe.production.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "production_audit_logs")
public class AuditLog {
    @Id
    private String id;
    private String entityId;
    private String entityType; // e.g. "JOB"
    private String action; // e.g. "START_JOB", "COMPLETE_STEP"
    private String details;
    private LocalDateTime timestamp;
    private String user;

    public AuditLog() {
    }

    public AuditLog(String entityId, String entityType, String action, String details, String user) {
        this.entityId = entityId;
        this.entityType = entityType;
        this.action = action;
        this.details = details;
        this.user = user;
        this.timestamp = LocalDateTime.now();
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
