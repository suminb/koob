package io.shortbread.koob.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

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
    private Integer room;

    @Getter
    @Setter
    private LocalDateTime startDatetime;

    @Getter
    @Setter
    private LocalDateTime endDatetime;

    public Reservation() {
    }

    public Reservation(int room, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        setRoom(room);
        setStartDatetime(startDateTime);
        setEndDatetime(endDateTime);
    }

    @Override
    public String toString() {
        return String.format("Reservation@%d{room=%d, start=%s, end=%s",
                getId(), getRoom(), getStartDatetime(), getEndDatetime());
    }
}
