package io.shortbread.koob.services;

import io.shortbread.koob.dao.ReservationDAO;
import io.shortbread.koob.exceptions.InvalidReservationRequestException;
import io.shortbread.koob.models.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Iterator;

@Service
public class ReservationService {

    @Autowired
    private ReservationDAO reservationDAO;

    /**
     * Ensures the datetime is specified in 30-min intervals
     * @param datetime
     */
    public boolean isValidHours(LocalDateTime datetime) {
        return datetime.getMinute() % 30 == 0;
    }

    public Reservation createReservation(int room, LocalDateTime startDateTime, LocalDateTime endDateTime)
            throws InvalidReservationRequestException {

        if (!isValidHours(startDateTime)) {
            throw new InvalidReservationRequestException("Start datetime must be given in 30-min intervals");
        }
        if (!isValidHours(endDateTime)) {
            throw new InvalidReservationRequestException("End datetime must be given in 30-min intervals");
        }

        // FIXME: We don't need the actual records; need a count only.
        Iterator<Reservation> overlappingReservations = reservationDAO.findOverlappings(room, startDateTime, endDateTime).iterator();
        if (overlappingReservations.hasNext()) {
            Reservation overlapped = overlappingReservations.next();
            throw new InvalidReservationRequestException(
                    String.format("Overlapping reservation exists: %s", overlapped));
        }
        Reservation reservation = new Reservation(room, startDateTime, endDateTime);
        reservationDAO.save(reservation);

        return reservation;
    }

    public Iterable<Reservation> getAllReservations() {
        return reservationDAO.findAll();
    }
}
