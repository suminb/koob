package io.shortbread.koob.dao;

import io.shortbread.koob.models.Reservation;
import org.springframework.data.repository.CrudRepository;

public interface ReservationDAO extends CrudRepository<Reservation, Long> {
}
