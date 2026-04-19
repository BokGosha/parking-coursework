package ru.bokgosha.parking.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "rents")
public class Rent {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    @Id
    @SequenceGenerator(name = "rents_seq", sequenceName = "rents_sequence", allocationSize = 1)
    @GeneratedValue(generator = "rents_seq", strategy = GenerationType.SEQUENCE)
    private Long id;

    private LocalDateTime startDate;
    private LocalDateTime finishDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    public long getRentDurationInSeconds() {
        if (startDate == null) {
            return 0;
        }
        LocalDateTime end = finishDate != null ? finishDate : LocalDateTime.now();
        return Duration.between(startDate, end).getSeconds();
    }

    public String getFormattedStartDate() {
        return startDate != null ? startDate.format(DATE_FORMAT) : null;
    }

    public String getFormattedFinishDate() {
        return finishDate != null ? finishDate.format(DATE_FORMAT) : null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Rent rent)) return false;
        return id != null && id.equals(rent.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
