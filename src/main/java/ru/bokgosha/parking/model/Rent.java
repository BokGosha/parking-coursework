package ru.bokgosha.parking.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.bokgosha.parking.DTO.RentDTO;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Entity
@Table(name = "rents")
@NoArgsConstructor
@AllArgsConstructor
public class Rent {

    @Id
    @SequenceGenerator(name = "rents_seq", sequenceName = "rents_sequence", allocationSize = 1)
    @GeneratedValue(generator = "rents_seq", strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "finish_date")
    private LocalDateTime finishDate;

    @JsonIgnore
    @ManyToOne
    private User user;

    @JsonIgnore
    @ManyToOne
    private Place place;

    public long getRentDurationInSeconds() {
        if (startDate != null && finishDate != null) {
            return Duration.between(startDate, finishDate).getSeconds();
        }

        if (startDate != null) {
            return Duration.between(startDate, LocalDateTime.now()).getSeconds();
        }

        return 0;
    }

    public String getFormattedStartDate() {
        if (startDate != null) {
            return startDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        }

        return null;
    }

    public String getFormattedFinishDate() {
        return finishDate != null ? finishDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) : null;
    }

    public RentDTO toDto() {
        return new RentDTO(id, user.getId(), place.getId(), startDate, finishDate);
    }
}
