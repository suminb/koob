package io.shortbread.koob.models;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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

    @Test
    public void testSynthesizeAllOccurrences1() {
        Reservation reservation = makeReservation("2018-11-11T10:00", "2018-11-11T11:00", 1, 3);

        List<Reservation> reservations = StreamSupport
                .stream(reservation.synthesizeAllOccurences().spliterator(), false)
                .collect(Collectors.toList());

        Assert.assertEquals(dt("2018-11-11T10:00"), reservations.get(0).getStart());
        Assert.assertEquals(dt("2018-11-11T11:00"), reservations.get(0).getEnd());

        Assert.assertEquals(dt("2018-11-18T10:00"), reservations.get(1).getStart());
        Assert.assertEquals(dt("2018-11-18T11:00"), reservations.get(1).getEnd());

        Assert.assertEquals(dt("2018-11-25T10:00"), reservations.get(2).getStart());
        Assert.assertEquals(dt("2018-11-25T11:00"), reservations.get(2).getEnd());
    }

    @Test
    public void testSynthesizeAllOccurrences2() {
        Reservation reservation = makeReservation("2018-11-01T08:00", "2018-11-01T10:00", 2, 2);

        List<Reservation> reservations = StreamSupport
                .stream(reservation.synthesizeAllOccurences().spliterator(), false)
                .collect(Collectors.toList());

        Assert.assertEquals(dt("2018-11-01T08:00"), reservations.get(0).getStart());
        Assert.assertEquals(dt("2018-11-01T10:00"), reservations.get(0).getEnd());

        Assert.assertEquals(dt("2018-11-15T08:00"), reservations.get(1).getStart());
        Assert.assertEquals(dt("2018-11-15T10:00"), reservations.get(1).getEnd());
    }
}
