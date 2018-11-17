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
     * @param room Meeting roomId number
     * @param startDatetime Specifies when the reservation begins (inclusive)
     * @param endDatetime Specifies when the reservation ends (exclusive)
     * @return
     */
    @CrossOrigin(CORS_SRC)
    @PostMapping("/reservations")
    public Reservation createReservation(
            @RequestParam(value = "roomId") int room,
            @RequestParam(value = "start_datetime")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDatetime,
            @RequestParam(value = "end_datetime")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDatetime
    ) throws InvalidRequestException {
        return reservationService.createReservation(room, startDatetime, endDatetime);
    }
}
