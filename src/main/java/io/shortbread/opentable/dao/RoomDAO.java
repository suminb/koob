package io.shortbread.opentable.dao;

import io.shortbread.opentable.models.Room;
import org.springframework.data.repository.CrudRepository;

public interface RoomDAO extends CrudRepository<Room, Long> {
}
