package io.shortbread.koob.models;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;

/**
 * Ensures Reservation.getDuration() works
 */
@RunWith(Parameterized.class)
public class ReservationDurationTests {
    @Parameterized.Parameters(name = "{index}: {0}, {1}, {2}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {"2018-11-17T15:00", "2018-11-17T15:00", 0},
                {"2018-11-17T03:00", "2018-11-17T03:30", 30},
                {"2018-11-17T15:00", "2018-11-17T16:00", 60},
                {"2018-11-17T23:30", "2018-11-18T01:30", 120},
        });
    }

    @Parameterized.Parameter
    public String start;

    @Parameterized.Parameter(1)
    public String end;

    @Parameterized.Parameter(2)
    public int duration;

    @Test
    public void testDuration() {
        Reservation reservation = new Reservation();
        reservation.setStart(LocalDateTime.parse(start));
        reservation.setEnd(LocalDateTime.parse(end));
        Assert.assertEquals(duration, reservation.getDuration());
    }
}
