package io.shortbread.koob.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.validation.constraints.NotBlank;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

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
    @Setter
    @JsonProperty("recurring_frequency")
    private RecurringFrequency recurringFrequency;

    @Getter
    @Setter
    @JsonProperty("recurring_interval")
    private int recurringInterval;

    @Getter
    @Setter
    @JsonProperty("recurring_count")
    private int recurringCount;

    public Reservation() {
    }

    @Override
    public String toString() {
        return String.format("Reservation@%d{room=%d, start=%s, end=%s",
                getId(), getRoomId(), getStart(), getEnd());
    }

    public long getDuration() {
        return start.until(end, ChronoUnit.MINUTES);
    }

    public LocalDateTime getLastOccurence() {
        RecurringFrequency freq = getRecurringFrequency();
        if (freq.equals(RecurringFrequency.None)) {
            return getStart();
        }
        else {
            if (freq.equals(RecurringFrequency.Weekly)) {
                return getStart()
                        .plus(getRecurringCount() * getRecurringInterval() * 7, ChronoUnit.DAYS);
            }
            else {
                throw new NotImplementedException();
            }
        }
    }
}
