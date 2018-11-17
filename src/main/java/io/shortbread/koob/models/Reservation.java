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
    private LocalDateTime startDateTime;

    @Getter
    @Setter
    private LocalDateTime endDateTime;

    public Reservation(int room, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        setRoom(room);
        setStartDateTime(startDateTime);
        setEndDateTime(endDateTime);
    }
}
