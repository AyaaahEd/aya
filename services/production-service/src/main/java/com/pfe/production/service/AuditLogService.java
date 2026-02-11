package com.pfe.production.service;

import com.pfe.production.domain.AuditLog;
import com.pfe.production.repository.AuditLogRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Async // Log asynchronously to avoid blocking main thread (requires @EnableAsync
           // configuration if used, but standard sync is safer for now)
    public void log(String entityId, String entityType, String action, String details, String user) {
        AuditLog log = new AuditLog(entityId, entityType, action, details, user);
        log.setTimestamp(LocalDateTime.now());
        auditLogRepository.save(log);
    }
}
