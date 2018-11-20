package io.shortbread.koob.services;

import io.shortbread.koob.config.AppConfig;
import io.shortbread.koob.exceptions.InvalidRequestException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDateTime;

@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = {AppConfig.class})
public class ReservationServiceTests {

//    @Autowired
//    private ReservationService reservationService;
    // NOTE: @Autowired seems having some issues with establishing a database connection
    // when run on the CI. Runs without any issue locally though.
    private ReservationService reservationService = new ReservationService();


    @Test
    public void testIsValidHours() {
        Assert.assertTrue(reservationService.isValidHours(LocalDateTime.parse("2018-11-17T11:00")));
        Assert.assertTrue(reservationService.isValidHours(LocalDateTime.parse("2018-11-17T13:30")));

        Assert.assertFalse(reservationService.isValidHours(LocalDateTime.parse("2018-11-17T00:01")));
    }

    // @Test
    public void testCreateReservation() throws InvalidRequestException {
        reservationService.createReservation(1, "Meeting title", "Meeting description",
                LocalDateTime.parse("2018-11-18T10:00"), LocalDateTime.parse("2018-11-18T11:00"));
    }
}
