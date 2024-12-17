package ru.bokgosha.parking.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.bokgosha.parking.DTO.PlaceDTO;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "places")
public class Place {

    @Id
    @SequenceGenerator(name = "cars_seq", sequenceName = "cars_sequence", allocationSize = 1)
    @GeneratedValue(generator = "cars_seq", strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "number")
    private int number;

    @Column(name = "available")
    private boolean available;

    @OneToMany(mappedBy = "place", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rent> rents = new ArrayList<>();

    public PlaceDTO toDto() {
        return new PlaceDTO(id, number, available, rents);
    }
}
