package com.pfe.production.domain.audit;

import com.pfe.production.domain.audit.AuditLog.FieldChange;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Async
    public void log(String entityId, String entityType, String action, String details, String user) {
        AuditLog log = new AuditLog(entityId, entityType, action, details, user);
        log.setTimestamp(LocalDateTime.now());
        auditLogRepository.save(log);
    }

    /**
     * Logs an action with field-level change tracking (before/after values).
     */
    @Async
    public void logWithChanges(String entityId, String entityType, String action,
            Map<String, FieldChange> changes, String user) {
        AuditLog log = new AuditLog(entityId, entityType, action, "Field changes recorded", changes, user);
        log.setTimestamp(LocalDateTime.now());
        auditLogRepository.save(log);
    }
}