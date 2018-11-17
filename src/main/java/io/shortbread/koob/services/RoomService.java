package io.shortbread.koob.services;

import io.shortbread.koob.dao.RoomDAO;
import io.shortbread.koob.models.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoomService {
    @Autowired
    private RoomDAO roomDAO;

    public Iterable<Room> getAll() {
        return roomDAO.findAll();
    }
}
