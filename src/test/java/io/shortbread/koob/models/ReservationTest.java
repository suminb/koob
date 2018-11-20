package io.shortbread.koob.models;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Iterator;

public class ReservationTest {
    private static LocalDateTime dt(String datetime) {
        return LocalDateTime.parse(datetime);
    }

    private static Reservation makeReservation(String start, String end, int interval, int count) {
        Reservation reservation = new Reservation();
        reservation.setStart(dt(start));
        reservation.setEnd(dt(end));
        reservation.setRecurringProperties(RecurringFrequency.Weekly, interval, count);

        return reservation;
    }

    private static void checkReservation(Iterator<Reservation> iter, String expectedStart, String expectedEnd) {
        Reservation reservation = iter.next();

        Assert.assertEquals(dt(expectedStart), reservation.getStart());
        Assert.assertEquals(dt(expectedEnd), reservation.getEnd());
    }

    @Test
    public void testSynthesizeAllOccurrences1() {
        Reservation reservation = makeReservation("2018-11-11T10:00", "2018-11-11T11:00", 1, 3);

        Iterator<Reservation> iter = reservation.synthesizeAllOccurences().iterator();
        checkReservation(iter, "2018-11-11T10:00","2018-11-11T11:00");
        checkReservation(iter, "2018-11-18T10:00","2018-11-18T11:00");
        checkReservation(iter, "2018-11-25T10:00","2018-11-25T11:00");
    }

    @Test
    public void testSynthesizeAllOccurrences2() {
        Reservation reservation = makeReservation("2018-11-01T08:00", "2018-11-01T10:00", 2, 2);

        Iterator<Reservation> iter = reservation.synthesizeAllOccurences().iterator();
        checkReservation(iter, "2018-11-01T08:00","2018-11-01T10:00");
        checkReservation(iter, "2018-11-15T08:00","2018-11-15T10:00");
    }
}
