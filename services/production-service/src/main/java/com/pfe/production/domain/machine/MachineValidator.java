package com.pfe.production.domain.machine;

import com.pfe.production.shared.exception.BusinessException;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Domain validator for Machine aggregate.
 * Encapsulates business rules from Section 12.6 of the DDD spec.
 */
@Component
public class MachineValidator {

    /**
     * Validates work times are valid (startTime < endTime for each entry).
     */
    public void validateWorkTimes(Machine.WorkTimes workTimes) {
        if (workTimes == null)
            return;

        if (workTimes.getRegular() != null) {
            for (Machine.RegularWorkTime rwt : workTimes.getRegular()) {
                if (rwt.getStartTime() != null && rwt.getEndTime() != null
                        && !rwt.getEndTime().isAfter(rwt.getStartTime())) {
                    throw new BusinessException(
                            "Work time end must be after start for " + rwt.getDayOfWeek() +
                                    ": " + rwt.getStartTime() + " → " + rwt.getEndTime());
                }
            }
        }

        if (workTimes.getOther() != null) {
            for (Machine.OtherWorkTime owt : workTimes.getOther()) {
                if (owt.getStartTime() != null && owt.getEndTime() != null
                        && !owt.getEndTime().isAfter(owt.getStartTime())) {
                    throw new BusinessException(
                            "Special work time end must be after start for " + owt.getType() +
                                    " on " + owt.getDate());
                }
            }
        }
    }

    /**
     * Validates reservations don't overlap with each other.
     */
    public void validateReservations(List<Machine.Reservation> reservations) {
        if (reservations == null || reservations.size() <= 1)
            return;

        for (int i = 0; i < reservations.size(); i++) {
            for (int j = i + 1; j < reservations.size(); j++) {
                Machine.Reservation a = reservations.get(i);
                Machine.Reservation b = reservations.get(j);
                if (a.getStartDate().isBefore(b.getEndDate())
                        && a.getEndDate().isAfter(b.getStartDate())) {
                    throw new BusinessException(
                            "Reservation overlap detected: [" +
                                    a.getStartDate() + " → " + a.getEndDate() + "] with [" +
                                    b.getStartDate() + " → " + b.getEndDate() + "]");
                }
            }
        }
    }

    /**
     * Validates a new reservation doesn't overlap with existing ones.
     */
    public void validateNewReservation(Machine.Reservation newReservation, List<Machine.Reservation> existing) {
        if (newReservation.getStartDate() == null || newReservation.getEndDate() == null) {
            throw new BusinessException("Reservation start and end dates are required");
        }
        if (!newReservation.getEndDate().isAfter(newReservation.getStartDate())) {
            throw new BusinessException("Reservation end date must be after start date");
        }

        if (existing != null) {
            for (Machine.Reservation r : existing) {
                if (newReservation.getStartDate().isBefore(r.getEndDate())
                        && newReservation.getEndDate().isAfter(r.getStartDate())) {
                    throw new BusinessException(
                            "Reservation overlaps with existing reservation: " + r.getReason());
                }
            }
        }
    }

    /**
     * Validates setup time > 0.
     */
    public void validateSetup(Machine.Setup setup) {
        if (setup != null && setup.getSetupTime() != null && setup.getSetupTime() <= 0) {
            throw new BusinessException("Setup time must be greater than 0, got: " + setup.getSetupTime());
        }
    }

    /**
     * Deleted machines cannot be assigned to new jobs.
     */
    public void validateNotDeleted(Machine machine) {
        if (Boolean.TRUE.equals(machine.getDeleted())) {
            throw new BusinessException("Cannot use a deleted machine: " + machine.getName());
        }
    }

    /**
     * Validates at least one speed is defined.
     */
    public void validateSpeeds(List<Machine.Speed> speeds) {
        if (speeds == null || speeds.isEmpty()) {
            throw new BusinessException("At least one speed configuration is required for a machine");
        }
    }
}