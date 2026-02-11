package com.pfe.production.shared.exception;

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

    /**
     * Convenience constructor for simple validation errors.
     */
    public BusinessException(String message) {
        super(message);
        this.code = "VALIDATION_ERROR";
        this.entityType = null;
        this.entityId = null;
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
