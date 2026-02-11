package com.pfe.production.exception;

public class BusinessException extends RuntimeException {
    private final String entityType;
    private final String entityId;
    private final String code;

    public BusinessException(String message, String code, String entityType, String entityId) {
        super(message);
        this.code = code;
        this.entityType = entityType;
        this.entityId = entityId;
    }

    public String getEntityType() {
        return entityType;
    }

    public String getEntityId() {
        return entityId;
    }

    public String getCode() {
        return code;
    }
}
