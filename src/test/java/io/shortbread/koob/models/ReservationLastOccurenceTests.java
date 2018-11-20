package io.shortbread.koob.models;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class ReservationLastOccurenceTests {
    @Parameterized.Parameters(name = "{index}: {0}, {1}, {2}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {"2018-11-17T15:00", "2018-11-17T16:00", RecurringFrequency.None, 0, 0, "2018-11-17T15:00"},
                {"2018-11-17T15:00", "2018-11-17T16:00", RecurringFrequency.Weekly, 1, 1, "2018-11-24T15:00"},
                {"2018-11-17T23:30", "2018-11-18T00:30", RecurringFrequency.Weekly, 1, 3, "2018-12-08T23:30"},
                {"2018-11-19T01:30", "2018-11-19T03:30", RecurringFrequency.Weekly, 2, 2, "2018-12-17T01:30"},
        });
    }

    @Parameterized.Parameter
    public String startDatetime;

    @Parameterized.Parameter(1)
    public String endDatetime;

    @Parameterized.Parameter(2)
    public RecurringFrequency recurringFrequency;

    @Parameterized.Parameter(3)
    public int recurringInterval;

    @Parameterized.Parameter(4)
    public int recurringCount;

    @Parameterized.Parameter(5)
    public String lastOccurence;

    @Test
    public void testLastOccurence() {
        Reservation reservation = new Reservation();
        reservation.setStart(LocalDateTime.parse(startDatetime));
        reservation.setEnd(LocalDateTime.parse(endDatetime));
        reservation.setRecurringFrequency(recurringFrequency);
        reservation.setRecurringInterval(recurringInterval);
        reservation.setRecurringCount(recurringCount);

        Assert.assertEquals(LocalDateTime.parse(lastOccurence), reservation.getLastOccurence());
    }
}
