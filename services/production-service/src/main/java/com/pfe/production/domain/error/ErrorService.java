package com.pfe.production.domain.error;
import com.pfe.production.shared.exception.BusinessException;
import com.pfe.production.domain.audit.AuditLogService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ErrorService {

    private final ErrorRepository errorRepository;
    private final AuditLogService auditLogService;

    public ErrorService(ErrorRepository errorRepository, AuditLogService auditLogService) {
        this.errorRepository = errorRepository;
        this.auditLogService = auditLogService;
    }

    @Transactional
    public ErrorEntry logError(String entityType, String entityId, String errorCode, String errorMessage,
            String stackTrace) {
        ErrorEntry error = new ErrorEntry();
        error.setEntityType(entityType);
        error.setEntityId(entityId);
        error.setErrorCode(errorCode);
        error.setErrorMessage(errorMessage);
        error.setStackTrace(stackTrace);
        error.setStatus(ErrorEntry.ErrorStatus.OPEN);
        error.setCreatedAt(LocalDateTime.now());

        // Also log to audit log for redundancy/traceability
        try {
            auditLogService.log(entityId != null ? entityId : "SYSTEM",
                    entityType != null ? entityType : "SYSTEM",
                    "ERROR",
                    errorMessage,
                    "system");
        } catch (Exception e) {
            // Ignore audit log failures when logging errors to prevent infinite loops
            System.err.println("Failed to audit log error: " + e.getMessage());
        }

        return errorRepository.save(error);
    }

    @Transactional
    public ErrorEntry resolveError(String id, String resolution, String resolvedBy) {
        ErrorEntry error = errorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Error not found: " + id));

        error.setStatus(ErrorEntry.ErrorStatus.RESOLVED);
        error.setResolution(resolution);
        error.setResolvedBy(resolvedBy);
        error.setResolvedAt(LocalDateTime.now());

        return errorRepository.save(error);
    }

    public List<ErrorEntry> findAll() {
        return errorRepository.findAll();
    }

    public List<ErrorEntry> findByStatus(ErrorEntry.ErrorStatus status) {
        return errorRepository.findByStatus(status);
    }

    public Optional<ErrorEntry> findById(String id) {
        return errorRepository.findById(id);
    }
}