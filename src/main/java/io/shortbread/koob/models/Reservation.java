package io.shortbread.koob.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.validation.constraints.NotBlank;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;

@Entity
@Table(name = "reservations")
public class Reservation {
    @Id
    @Getter
    @Setter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Getter
    @Setter
    @JsonProperty("room_id")
    private Integer roomId;

    @Getter
    @Setter
    @NotBlank
    private String subject;

    @Getter
    @Setter
    private String description;

    @Getter
    @Setter
    @JsonProperty("start")
    private LocalDateTime start;

    @Getter
    @Setter
    @JsonProperty("end")
    private LocalDateTime end;

    @Getter
    @JsonProperty("recurring_frequency")
    private RecurringFrequency recurringFrequency;

    @Getter
    @JsonProperty("recurring_interval")
    private int recurringInterval;

    @Getter
    @JsonProperty("recurring_count")
    private int recurringCount;

    public Reservation() {
    }

    public void setRecurringProperties(RecurringFrequency recurringFrequency, int recurringInterval, int recurringCount) {
        this.recurringFrequency = recurringFrequency;
        this.recurringInterval = recurringInterval;
        this.recurringCount = recurringCount;

        this.closing = getLastOccurence().plus(getDuration(), ChronoUnit.MINUTES);
    }

    @Override
    public String toString() {
        return String.format("Reservation@%d{room=%d, start=%s, end=%s",
                getId(), getRoomId(), getStart(), getEnd());
    }

    /**
     * Duration of the reservation in terms of minuutes
     */
    public long getDuration() {
        return start.until(end, ChronoUnit.MINUTES);
    }

    public static LocalDateTime nextRecurrenceDatetime(LocalDateTime current, RecurringFrequency frequency, int interval) {
        if (frequency.equals(RecurringFrequency.Weekly)) {
            return current.plus(interval, ChronoUnit.WEEKS);
        }
        else {
            throw new NotImplementedException();
        }
    }

    public LocalDateTime getLastOccurence() {
        RecurringFrequency freq = getRecurringFrequency();
        if (freq.equals(RecurringFrequency.None)) {
            return getStart();
        }
        else {
            if (freq.equals(RecurringFrequency.Weekly)) {
                return getStart()
                        .plus(getRecurringCount() * getRecurringInterval(), ChronoUnit.WEEKS);
            }
            else {
                throw new NotImplementedException();
            }
        }
    }

    public Iterable<Reservation> synthesizeAllOccurences() {
        Iterator<Reservation> iter = new Iterator<Reservation>() {
            private LocalDateTime current = getStart();
            private int remainingRecurrences = getRecurringCount();

            @Override
            public boolean hasNext() {
                return remainingRecurrences > 0;
            }

            @Override
            public Reservation next() {
                Reservation reservation = new Reservation();
                reservation.setRoomId(getRoomId());
                reservation.setStart(current);
                reservation.setEnd(current.plus(getDuration(), ChronoUnit.MINUTES));
                reservation.setSubject(getSubject());
                reservation.setDescription(getDescription());

                current = nextRecurrenceDatetime(current, getRecurringFrequency(), getRecurringInterval());
                remainingRecurrences--;

                return reservation;
            }
        };
        return () -> iter;
    }
}
