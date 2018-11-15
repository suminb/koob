package io.shortbread.opentable.dao;

import io.shortbread.opentable.models.Reservation;
import org.springframework.data.repository.CrudRepository;

public interface ReservationDAO extends CrudRepository<Reservation, Long> {
}
