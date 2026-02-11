package com.pfe.production.shared.valueobject;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Value Object representing a date-time range (start to end).
 * Validates that end is strictly after start.
 * Provides overlap detection for scheduling.
 */
public final class DateTimeRange {

    private final LocalDateTime start;
    private final LocalDateTime end;

    public DateTimeRange(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("Start and end dates cannot be null");
        }
        if (!end.isAfter(start)) {
            throw new IllegalArgumentException(
                    "End date must be after start date. Start: " + start + ", End: " + end);
        }
        this.start = start;
        this.end = end;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    /**
     * Checks if this range overlaps with another range.
     * Two ranges overlap if one starts before the other ends AND ends after the
     * other starts.
     */
    public boolean overlaps(DateTimeRange other) {
        return this.start.isBefore(other.end) && this.end.isAfter(other.start);
    }

    /**
     * Checks if a given date-time falls within this range (inclusive start,
     * exclusive end).
     */
    public boolean contains(LocalDateTime dateTime) {
        return !dateTime.isBefore(start) && dateTime.isBefore(end);
    }

    /**
     * Checks if the start date is in the future relative to the given reference
     * time.
     */
    public boolean startsAfter(LocalDateTime reference) {
        return start.isAfter(reference);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        DateTimeRange that = (DateTimeRange) o;
        return Objects.equals(start, that.start) && Objects.equals(end, that.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }

    @Override
    public String toString() {
        return start + " → " + end;
    }
}
