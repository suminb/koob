package io.shortbread.koob.service;

import io.shortbread.koob.dao.ReservationDAO;
import io.shortbread.koob.models.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservationService {

    @Autowired
    private ReservationDAO reservationDAO;

    public Reservation createReservation(int room) {
        Reservation reservation = new Reservation(room);
        reservationDAO.save(reservation);

        return reservation;
    }

    public Iterable<Reservation> getAllReservations() {
        return reservationDAO.findAll();
    }
}
