package com.pfe.production.shared.valueobject;

import java.util.Objects;

/**
 * Value Object representing a positive quantity (repetition, planned/actual
 * quantities).
 * Enforces that value must be strictly greater than 0.
 */
public final class Quantity {

    private final int value;

    public Quantity(int value) {
        if (value <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0, got: " + value);
        }
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public boolean isGreaterThan(Quantity other) {
        return this.value > other.value;
    }

    public boolean isLessThanOrEqual(Quantity other) {
        return this.value <= other.value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Quantity quantity = (Quantity) o;
        return value == quantity.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
