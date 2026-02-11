package com.pfe.production.domain.machine;

import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class WorkScheduleService {

    public boolean isWithinWorkHours(Machine machine, LocalDateTime start, LocalDateTime end) {
        if (machine.getWorkTimes() == null || machine.getWorkTimes().getRegular() == null) {
            return false;
        }

        // 1. Check if it's a regular work day
        DayOfWeek day = start.getDayOfWeek();
        Machine.RegularWorkTime schedule = machine.getWorkTimes().getRegular().stream()
                .filter(r -> r.getDayOfWeek().equalsIgnoreCase(day.name()))
                .findFirst()
                .orElse(null);

        if (schedule == null) {
            return false;
        }

        // 2. Check times (Simple implementation: task must fit entirely within one
        // day's slot)
        // For multi-day tasks, logic would need to be split.
        LocalTime taskStart = start.toLocalTime();
        LocalTime taskEnd = end.toLocalTime();

        // Handle midnight crossing or multi-day if needed, but for now strict
        // single-shift check
        if (!start.toLocalDate().isEqual(end.toLocalDate())) {
            // If spanning days, we'd need more complex logic.
            // checks if both start and end are within the *same* day's window?
            // Or just reject multi-day for a single "isWithin"?
            return false;
        }

        return !taskStart.isBefore(schedule.getStartTime()) && !taskEnd.isAfter(schedule.getEndTime());
    }
}