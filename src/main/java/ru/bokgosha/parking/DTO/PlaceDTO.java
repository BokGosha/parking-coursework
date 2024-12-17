package ru.bokgosha.parking.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.bokgosha.parking.model.Rent;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaceDTO {

    private Long id;
    private int number;
    private boolean available;
    private List<Rent> rents = new ArrayList<>();
}
