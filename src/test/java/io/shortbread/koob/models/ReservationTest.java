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

    @Test
    public void testSynthesizeAllOccurrences() {
        Reservation reservation = new Reservation();
        reservation.setStart(LocalDateTime.parse("2018-11-11T10:00"));
        reservation.setEnd(LocalDateTime.parse("2018-11-11T11:00"));
        reservation.setRecurringProperties(RecurringFrequency.Weekly, 1, 3);

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
}
