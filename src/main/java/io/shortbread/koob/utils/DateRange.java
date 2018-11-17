package io.shortbread.koob.utils;

import java.time.LocalDate;

public class DateRange extends TemporalRange {
    public DateRange(LocalDate lowerbound, LocalDate upperbound) throws Exception {
        super(lowerbound, upperbound);
    }

    public static DateRange build(LocalDate lowerbound, LocalDate upperbound) throws Exception {
        DateRange range = new DateRange(lowerbound, upperbound);
        return range;
    }
}
