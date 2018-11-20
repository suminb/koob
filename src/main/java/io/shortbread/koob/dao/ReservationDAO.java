package io.shortbread.koob.dao;

import io.shortbread.koob.models.Reservation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;

public interface ReservationDAO extends CrudRepository<Reservation, Long> {

    @Query(
            value = "SELECT * FROM reservations WHERE room_id = ?1 AND `end` > ?2 AND start < ?3",
            countQuery = "SELECT count(1) FROM reservations WHERE room_id = ?1 AND `end` > ?2 AND start < ?3",
            nativeQuery = true)
    Iterable<Reservation> findOverlappings(int room, LocalDateTime startDateTime, LocalDateTime endDateTime);
}
