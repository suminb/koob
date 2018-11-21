package io.shortbread.koob.dao;

import io.shortbread.koob.models.Reservation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface ReservationDAO extends CrudRepository<Reservation, Long> {

    @Query(
            value = "SELECT * FROM reservations WHERE room_id = ?1 AND `end` > ?2 AND start < ?3",
            countQuery = "SELECT count(1) FROM reservations WHERE room_id = ?1 AND `end` > ?2 AND start < ?3",
            nativeQuery = true)
    Iterable<Reservation> findOverlappings(int room, LocalDateTime start, LocalDateTime end);

    @Query(
            //value = "SELECT * FROM reservations WHERE (start BETWEEN ?1 AND ?2) OR (closing BETWEEN ?1 AND ?2)",
            value = "SELECT * FROM reservations",
            nativeQuery = true
    )
    Iterable<Reservation> findReservationsBetween(LocalDateTime lowerbound, LocalDateTime upperbound);
}
