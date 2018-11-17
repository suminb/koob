package io.shortbread.koob;

import io.shortbread.koob.exceptions.InvalidRequestException;
import io.shortbread.koob.models.Reservation;
import io.shortbread.koob.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;

@RestController
public class MainController {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private ReservationService reservationService;

    @GetMapping("/")
    public String index() throws SQLException {
        Connection conn = dataSource.getConnection();

        System.out.println(conn);

        return "Hello World!";
    }

    @GetMapping("/reservations")
    public Iterable<Reservation> getAllReservations() {
        return reservationService.getAllReservations();
    }

    /**
     *
     * @param room Meeting room number
     * @param startDatetime Specifies when the reservation begins (inclusive)
     * @param endDatetime Specifies when the reservation ends (exclusive)
     * @return
     */
    @PostMapping("/reservations")
    public Reservation createReservation(
            @RequestParam(value = "room") int room,
            @RequestParam(value = "start_datetime")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDatetime,
            @RequestParam(value = "end_datetime")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDatetime
    ) throws InvalidRequestException {
        return reservationService.createReservation(room, startDatetime, endDatetime);
    }
}
