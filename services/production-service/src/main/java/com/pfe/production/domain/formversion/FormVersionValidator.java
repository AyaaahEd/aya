package com.pfe.production.domain.formversion;

import com.pfe.production.domain.formversion.FormVersion.FormVersionState;
import com.pfe.production.shared.exception.BusinessException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Domain validator for FormVersion aggregate.
 * Encapsulates business rules from Section 12.2 of the DDD spec.
 */
@Component
public class FormVersionValidator {

    /**
     * Validates planning dates.
     */
    public void validatePlanning(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate != null && startDate.isBefore(LocalDateTime.now())) {
            throw new BusinessException("Planning start date must be in the future, got: " + startDate);
        }
        if (startDate != null && endDate != null && !endDate.isAfter(startDate)) {
            throw new BusinessException(
                    "Planning end date must be after start date. Start: " + startDate + ", End: " + endDate);
        }
    }

    /**
     * Validates a state transition is allowed.
     * Valid transitions: DRAFT → VALIDATED → IN_PRODUCTION → COMPLETED
     * Any state → CANCELLED (except COMPLETED)
     */
    public void validateStateTransition(FormVersionState from, FormVersionState to) {
        if (from == to)
            return;

        boolean valid = switch (from) {
            case DRAFT -> to == FormVersionState.VALIDATED || to == FormVersionState.CANCELLED;
            case VALIDATED -> to == FormVersionState.IN_PRODUCTION || to == FormVersionState.CANCELLED;
            case IN_PRODUCTION -> to == FormVersionState.COMPLETED || to == FormVersionState.CANCELLED;
            case COMPLETED, CANCELLED -> false;
        };

        if (!valid) {
            throw new BusinessException(
                    "Invalid state transition for FormVersion: " + from + " → " + to);
        }
    }

    /**
     * Only DRAFT versions can be validated.
     */
    public void validateCanValidate(FormVersion version) {
        if (version.getState() != FormVersionState.DRAFT) {
            throw new BusinessException(
                    "Only DRAFT versions can be validated. Current state: " + version.getState());
        }
    }

    /**
     * Only VALIDATED versions can move to IN_PRODUCTION.
     */
    public void validateCanStartProduction(FormVersion version) {
        if (version.getState() != FormVersionState.VALIDATED) {
            throw new BusinessException(
                    "Only VALIDATED versions can start production. Current state: " + version.getState());
        }
    }

    /**
     * Validates a version is not deleted.
     */
    public void validateNotDeleted(FormVersion version) {
        if (Boolean.TRUE.equals(version.getDeleted())) {
            throw new BusinessException("Cannot modify a deleted form version");
        }
    }
}