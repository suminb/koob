package io.shortbread.opentable.service;

import io.shortbread.opentable.dao.ReservationDAO;
import io.shortbread.opentable.models.Reservation;
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
