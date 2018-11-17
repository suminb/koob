package io.shortbread.koob.utils;

import org.junit.Test;

public class DateRangeValidationTests {

    @Test
    public void testBuild() throws Exception {
        DateRange.build("2018-11-01", "2018-11-01");
        DateRange.build("2018-11-01", "2018-11-02");
    }

    /**
     * Ensures the lowerbound must be earlier than the upperbound
     * @throws Exception
     */
    @Test(expected = InvalidTemporalRangeException.class)
    public void testInvalidRanges() throws Exception {
        DateRange.build("2018-11-02", "2018-11-01");
    }
}
