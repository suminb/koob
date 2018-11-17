package io.shortbread.koob.services;

import io.shortbread.koob.config.AppConfig;
import io.shortbread.koob.exceptions.InvalidRequestException;
import io.shortbread.koob.models.Reservation;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
public class ReservationListTests {
    @Autowired
    private ReservationService reservationService;

    @Before
    public void setup() throws InvalidRequestException {
        List<Object[]> params = Arrays.asList(new Object[][] {
                {"2018-11-16T12:00", "2018-11-16T13:00"},
                {"2018-11-17T03:00", "2018-11-17T03:30"},
                {"2018-11-17T15:00", "2018-11-17T16:00"},
                {"2018-11-17T23:30", "2018-11-18T01:30"},
        });
        for (Object[] param : params) {
            String start = (String) param[0];
            String end = (String) param[1];
            reservationService.createReservation(0, LocalDateTime.parse(start), LocalDateTime.parse(end));
        }
    }

    @Test
    public void testFindReservationsBetween() {
        Iterable<Reservation> reservationsIter = reservationService.findReservationsBetween(
                LocalDate.parse("2018-11-15"), LocalDate.parse("2018-11-15"));
        List<Reservation> reservations = Lists.newArrayList(reservationsIter);
        Assert.assertEquals(0, reservations.size());
    }

    // TODO: Add more comprehensive tests
}
