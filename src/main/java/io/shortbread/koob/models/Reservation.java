package io.shortbread.koob.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

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
    @JsonProperty("start_datetime")
    private LocalDateTime startDatetime;

    @Getter
    @Setter
    @JsonProperty("end_datetime")
    private LocalDateTime endDatetime;

    public Reservation() {
    }

    public Reservation(int roomId, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        setRoomId(roomId);
        setStartDatetime(startDateTime);
        setEndDatetime(endDateTime);
    }

    @Override
    public String toString() {
        return String.format("Reservation@%d{room=%d, start=%s, end=%s",
                getId(), getRoomId(), getStartDatetime(), getEndDatetime());
    }

    public long getDuration() {
        return startDatetime.until(endDatetime, ChronoUnit.MINUTES);
    }
}
