package io.shortbread.koob.services;

import io.shortbread.koob.config.AppConfig;
import io.shortbread.koob.exceptions.InvalidRequestException;
import io.shortbread.koob.models.RecurringFrequency;
import io.shortbread.koob.models.Reservation;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDateTime;
import java.util.Iterator;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
public class ReservationServiceTests {

    private static LocalDateTime dt(String datetime) {
        return LocalDateTime.parse(datetime);
    }

    @Autowired
    private ReservationService reservationService;
    // NOTE: @Autowired seems having some issues with establishing a database connection
    // when run on the CI. Runs without any issue locally though.
    //private ReservationService reservationService = new ReservationService();


    @Test
    public void testIsValidHours() {
        Assert.assertTrue(reservationService.isValidHours(dt("2018-11-17T11:00")));
        Assert.assertTrue(reservationService.isValidHours(dt("2018-11-17T13:30")));

        Assert.assertFalse(reservationService.isValidHours(dt("2018-11-17T00:01")));
    }

    @Test
    public void testCreateReservation() throws InvalidRequestException {
        reservationService.createReservation(1, "Meeting title", "Meeting description",
                dt("2018-11-18T08:00"), dt("2018-11-18T09:00"),
                RecurringFrequency.None, 0, 0);
    }

    private Reservation makeReservation(String start, String end, int interval, int count) throws InvalidRequestException {
        return reservationService.createReservation(
                1, "Weekly meeting", "Meeting description",
                dt(start), dt(end), RecurringFrequency.Weekly, interval, count);
    }

    private void assertRecurrenceCount(String lowerbound, String upperbound, int expectedCount) {
        Iterable<Reservation> reservations = reservationService
                .findReservationsBetween(dt(lowerbound), dt(upperbound));
        int count = 0;
        for (Iterator<Reservation> iter = reservations.iterator(); iter.hasNext(); iter.next(), count++) {}

        Assert.assertEquals(expectedCount, count);
    }

    @Test
    public void testRecurringEvents() throws InvalidRequestException {
        Reservation reservation = makeReservation("2018-11-18T10:00", "2018-11-18T11:00", 1, 2);

        Assert.assertEquals(dt("2018-12-02T11:00"), reservation.getClosing());

        assertRecurrenceCount("2018-11-01T00:00", "2018-11-10T23:30", 0);
        assertRecurrenceCount("2018-11-01T00:00", "2018-11-20T23:30", 1);
    }
}
