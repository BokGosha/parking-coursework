package ru.bokgosha.parking.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "places")
public class Place {

    @Id
    @SequenceGenerator(name = "places_seq", sequenceName = "places_sequence", allocationSize = 1)
    @GeneratedValue(generator = "places_seq", strategy = GenerationType.SEQUENCE)
    private Long id;

    private int number;
    private boolean available;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Place place)) return false;
        return id != null && id.equals(place.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
