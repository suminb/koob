package io.shortbread.koob;

import io.shortbread.koob.services.InvalidRequestException;
import io.shortbread.koob.models.Reservation;
import io.shortbread.koob.services.ReservationService;
import io.shortbread.koob.utils.DateRange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
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
    public Iterable<Reservation> getReservations(
            @RequestParam(value = "date_lowerbound")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateLowerbound,
            @RequestParam(value = "date_upperbound")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateUpperbound
    ) throws Exception {
        return reservationService.findReservationsBetween(DateRange.build(dateLowerbound, dateUpperbound));
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
