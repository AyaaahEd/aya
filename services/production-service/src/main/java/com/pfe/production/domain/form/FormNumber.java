package com.pfe.production.domain.form;

import java.util.Objects;

/**
 * Value Object representing a form number in the format F{YEAR}-{SEQUENCE}.
 * Example: F2026-0001, F2026-0002
 */
public final class FormNumber {

    private final String value;

    public FormNumber(String value) {
        if (value == null || !value.matches("F\\d{4}-\\d{4}")) {
            throw new IllegalArgumentException(
                    "Invalid form number format. Expected F{YEAR}-{SEQUENCE} (e.g., F2026-0001), got: " + value);
        }
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public int getYear() {
        return Integer.parseInt(value.substring(1, 5));
    }

    public int getSequence() {
        return Integer.parseInt(value.substring(6));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        FormNumber that = (FormNumber) o;
        return Objects.equals(value, that.value);
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
