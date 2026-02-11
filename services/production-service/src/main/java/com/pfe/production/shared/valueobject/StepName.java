package com.pfe.production.shared.valueobject;

import java.util.Objects;
import java.util.Set;

/**
 * Value Object representing a production step name.
 * Validates against the known production steps: PRINTING, CUTTING, SEWING,
 * COATING.
 */
public final class StepName {

    public static final String PRINTING = "PRINTING";
    public static final String CUTTING = "CUTTING";
    public static final String SEWING = "SEWING";
    public static final String COATING = "COATING";

    private static final Set<String> VALID_STEPS = Set.of(PRINTING, CUTTING, SEWING, COATING);

    private final String value;

    public StepName(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Step name cannot be null or blank");
        }
        String normalized = value.toUpperCase().trim();
        if (!VALID_STEPS.contains(normalized)) {
            throw new IllegalArgumentException(
                    "Invalid step name: " + value + ". Must be one of: " + VALID_STEPS);
        }
        this.value = normalized;
    }

    public String getValue() {
        return value;
    }

    public boolean isPrinting() {
        return PRINTING.equals(value);
    }

    public boolean isCutting() {
        return CUTTING.equals(value);
    }

    public boolean isSewing() {
        return SEWING.equals(value);
    }

    public boolean isCoating() {
        return COATING.equals(value);
    }

    public static boolean isValid(String name) {
        return name != null && VALID_STEPS.contains(name.toUpperCase().trim());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        StepName stepName = (StepName) o;
        return Objects.equals(value, stepName.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
