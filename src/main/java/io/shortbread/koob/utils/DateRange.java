package io.shortbread.koob.utils;

import java.time.LocalDate;

public class DateRange extends TemporalRange {
    public DateRange(LocalDate lowerbound, LocalDate upperbound) throws InvalidTemporalRangeException {
        super(lowerbound, upperbound);
    }

    public static DateRange build(LocalDate lowerbound, LocalDate upperbound) throws InvalidTemporalRangeException {
        DateRange range = new DateRange(lowerbound, upperbound);
        return range;
    }

    public static DateRange build(String lowerbound, String upperbound) throws InvalidTemporalRangeException {
        return build(LocalDate.parse(lowerbound), LocalDate.parse(upperbound));
    }
}
