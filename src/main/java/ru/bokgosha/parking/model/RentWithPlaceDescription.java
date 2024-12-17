package ru.bokgosha.parking.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RentWithPlaceDescription {

    private Rent rent;
    private String placeDescription;
}
