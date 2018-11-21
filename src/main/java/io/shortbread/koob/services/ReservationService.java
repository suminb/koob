package io.shortbread.koob.services;

import io.shortbread.koob.dao.ReservationDAO;
import io.shortbread.koob.exceptions.InvalidReservationRequestException;
import io.shortbread.koob.models.RecurringFrequency;
import io.shortbread.koob.models.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ReservationService {

    /**
     * Minimum duration of a meeting (in terms of minutes)
     */
    public static final int MIN_DURATION = 30;

    @Autowired
    private ReservationDAO reservationDAO;

    /**
     * Ensures the datetime is specified in 30-min intervals
     * @param datetime
     */
    public boolean isValidHours(LocalDateTime datetime) {
        return datetime.getMinute() % 30 == 0;
    }

    public Reservation createReservation(
            int room, String subject, String description, LocalDateTime start, LocalDateTime end,
            RecurringFrequency recurringFrequency, int recurringInterval, int recurringCount
    ) throws InvalidReservationRequestException {

        if (!isValidHours(start)) {
            throw new InvalidReservationRequestException("Start datetime must be given in 30-min intervals");
        }
        if (!isValidHours(end)) {
            throw new InvalidReservationRequestException("End datetime must be given in 30-min intervals");
        }
        if (!end.isAfter(start)) {
            throw new InvalidReservationRequestException("Start datetime must be earlier than end datetime");
        }

        // FIXME: We don't need the actual records; need a count only.
        Iterator<Reservation> overlappingReservations = reservationDAO.findOverlappings(room, start, end).iterator();
        if (overlappingReservations.hasNext()) {
            Reservation overlapped = overlappingReservations.next();
            throw new InvalidReservationRequestException(
                    String.format("Overlapping reservation exists: %s", overlapped));
        }
        Reservation reservation = new Reservation();
        reservation.setRoomId(room);
        reservation.setSubject(subject);
        reservation.setDescription(description);
        reservation.setStart(start);
        reservation.setEnd(end);

        // TODO: Validation
        reservation.setRecurringProperties(recurringFrequency, recurringInterval, recurringCount);

        // TODO: Make weekly_recurrences entry

        if (reservation.getDuration() < MIN_DURATION) {
            throw new InvalidReservationRequestException(
                    String.format("Meeting must be at least %s minutes long", MIN_DURATION));
        }
        reservationDAO.save(reservation);

        return reservation;
    }

    public Iterable<Reservation> getAllReservations() {
        return reservationDAO.findAll();
    }

    public Iterable<Reservation> findReservationsBetween(LocalDateTime lowerbound, LocalDateTime upperbound) {
        // TODO: Figure out weekday and query for it (weekly_recurrences)
        Iterable<Reservation> candidates = reservationDAO.findReservationsBetween(lowerbound, upperbound);
        return StreamSupport.stream(candidates.spliterator(), false)
                .filter(reservation -> reservation.isBetween(lowerbound, upperbound, true))
                .collect(Collectors.toList());
    }
}
