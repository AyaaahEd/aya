package com.pfe.production.domain.form;

import com.pfe.production.domain.job.Job;
import com.pfe.production.shared.exception.BusinessException;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Domain validator for Form aggregate.
 * Encapsulates business rules from Section 12.1 of the DDD spec.
 */
@Component
public class FormValidator {

    /**
     * Validates a form for creation.
     */
    public void validateForCreation(Form form) {
        validateRepetition(form.getRepetition());
        validateSize(form.getSize());
        validateHasQualityConfig(form.getConfigurationProperties());
    }

    /**
     * Validates a form can be updated.
     */
    public void validateForUpdate(Form form) {
        validateNotDeleted(form);
    }

    /**
     * Validates a form can be deleted (no active jobs referencing it).
     */
    public void validateCanDelete(Form form, List<Job> activeJobs) {
        validateNotDeleted(form);
        if (activeJobs != null && !activeJobs.isEmpty()) {
            throw new BusinessException("Cannot delete form with " + activeJobs.size() + " active jobs");
        }
    }

    /**
     * Repetition must be > 0.
     */
    public void validateRepetition(Integer repetition) {
        if (repetition == null || repetition <= 0) {
            throw new BusinessException("Repetition must be greater than 0, got: " + repetition);
        }
    }

    /**
     * Size width and height must be > 0.
     */
    public void validateSize(Form.Size size) {
        if (size == null) {
            throw new BusinessException("Form size is required");
        }
        if (size.getWidth() == null || size.getWidth() <= 0) {
            throw new BusinessException("Form width must be greater than 0, got: " + size.getWidth());
        }
        if (size.getHeight() == null || size.getHeight() <= 0) {
            throw new BusinessException("Form height must be greater than 0, got: " + size.getHeight());
        }
    }

    /**
     * At least one configuration property of type "Quality" is required.
     */
    public void validateHasQualityConfig(List<Form.ConfigurationProperty> configProps) {
        if (configProps == null || configProps.isEmpty()) {
            throw new BusinessException("At least one configuration property is required");
        }
        boolean hasQuality = configProps.stream()
                .anyMatch(cp -> "Quality".equalsIgnoreCase(cp.getType()));
        if (!hasQuality) {
            throw new BusinessException("At least one configuration property of type 'Quality' is required");
        }
    }

    /**
     * Deleted forms cannot be updated.
     */
    public void validateNotDeleted(Form form) {
        if (Boolean.TRUE.equals(form.getDeleted())) {
            throw new BusinessException("Cannot modify a deleted form: " + form.getFormNumber());
        }
    }
}