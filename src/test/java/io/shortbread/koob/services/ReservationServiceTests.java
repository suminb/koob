package io.shortbread.koob.services;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import java.time.LocalDateTime;

@RunWith(SpringJUnit4ClassRunner.class)
public class ReservationServiceTests {

    private ReservationService reservationService = new ReservationService();

    @Test
    public void testIsValidHours() {
        Assert.assertTrue(reservationService.isValidHours(LocalDateTime.parse("2018-11-17T11:00")));
        Assert.assertTrue(reservationService.isValidHours(LocalDateTime.parse("2018-11-17T13:30")));

        Assert.assertFalse(reservationService.isValidHours(LocalDateTime.parse("2018-11-17T00:01")));
    }
}
