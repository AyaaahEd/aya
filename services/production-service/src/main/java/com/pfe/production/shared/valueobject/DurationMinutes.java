package com.pfe.production.shared.valueobject;

import java.util.Objects;

/**
 * Value Object representing a duration in minutes.
 * Enforces that value must be >= 0.
 */
public final class DurationMinutes {

    private static final DurationMinutes ZERO = new DurationMinutes(0);

    private final int value;

    public DurationMinutes(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("Duration in minutes cannot be negative, got: " + value);
        }
        this.value = value;
    }

    public static DurationMinutes zero() {
        return ZERO;
    }

    public int getValue() {
        return value;
    }

    public DurationMinutes add(DurationMinutes other) {
        return new DurationMinutes(this.value + other.value);
    }

    public DurationMinutes multiply(int factor) {
        return new DurationMinutes(this.value * factor);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        DurationMinutes that = (DurationMinutes) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value + " min";
    }
}
