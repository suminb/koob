package io.shortbread.koob;

import io.shortbread.koob.exceptions.InvalidRequestException;
import io.shortbread.koob.models.Reservation;
import io.shortbread.koob.models.Room;
import io.shortbread.koob.services.ReservationService;
import io.shortbread.koob.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;

@RestController
public class MainController {

    // FIXME: Avoid using hard-coded value
    private static final String CORS_SRC = "http://localhost:3000";

    @Autowired
    private DataSource dataSource;

    @Autowired
    private RoomService roomService;

    @Autowired
    private ReservationService reservationService;

    @GetMapping("/")
    public String index() throws SQLException {
        Connection conn = dataSource.getConnection();

        System.out.println(conn);

        return "Hello World!";
    }

    @CrossOrigin(CORS_SRC)
    @GetMapping("/rooms")
    public Iterable<Room> getAllRooms() {
        return roomService.getAll();
    }

    @CrossOrigin(CORS_SRC)
    @GetMapping("/reservations")
    public Iterable<Reservation> getAllReservations() {
        return reservationService.getAllReservations();
    }

    /**
     *
     * @param roomId Meeting room ID
     * @param start Specifies when the reservation begins (inclusive)
     * @param end Specifies when the reservation ends (exclusive)
     * @return
     */
    @CrossOrigin(CORS_SRC)
    @PostMapping("/reservations")
    public Reservation createReservation(
            @RequestParam(value = "room_id") int roomId,
            @RequestParam(value = "subject") String subject,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "start")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(value = "end")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end
    ) throws InvalidRequestException {
        return reservationService.createReservation(roomId, subject, description, start, end);
    }
}
