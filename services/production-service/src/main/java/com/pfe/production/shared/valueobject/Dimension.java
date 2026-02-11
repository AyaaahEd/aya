package com.pfe.production.shared.valueobject;

import java.util.Objects;

/**
 * Value Object representing a physical dimension (width, height, length).
 * Enforces that value must be strictly greater than 0.
 */
public final class Dimension {

    private final double value;

    public Dimension(double value) {
        if (value <= 0) {
            throw new IllegalArgumentException("Dimension must be greater than 0, got: " + value);
        }
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Dimension dimension = (Dimension) o;
        return Double.compare(dimension.value, value) == 0;
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
