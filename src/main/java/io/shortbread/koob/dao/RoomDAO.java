package io.shortbread.koob.dao;

import io.shortbread.koob.models.Room;
import org.springframework.data.repository.CrudRepository;

public interface RoomDAO extends CrudRepository<Room, Long> {
}
