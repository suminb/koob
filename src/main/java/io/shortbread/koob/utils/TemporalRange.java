package io.shortbread.koob.utils;

import lombok.Getter;
import lombok.Setter;

import java.time.chrono.ChronoLocalDate;

public abstract class TemporalRange {
    @Getter
    @Setter
    protected ChronoLocalDate lowerbound;

    @Getter
    @Setter
    protected ChronoLocalDate upperbound;

    public TemporalRange(ChronoLocalDate lowerbound, ChronoLocalDate upperbound) throws InvalidTemporalRangeException {
        setLowerbound(lowerbound);
        setUpperbound(upperbound);
        validate();
    }

    public void validate() throws InvalidTemporalRangeException {
        if (lowerbound.isAfter(upperbound)) {
            throw new InvalidTemporalRangeException("Lowerbound must be earlier than upperbound");
        }
    }
}
