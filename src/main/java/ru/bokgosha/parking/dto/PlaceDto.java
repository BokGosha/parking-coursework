package ru.bokgosha.parking.dto;

import lombok.Data;
import ru.bokgosha.parking.model.Place;
import ru.bokgosha.parking.model.Rent;

import java.util.List;

@Data
public class PlaceDto {

    private Long id;
    private int number;
    private boolean available;
    private List<RentDto> rents = List.of();

    public static PlaceDto from(Place place) {
        PlaceDto placeDto = new PlaceDto();
        placeDto.setId(place.getId());
        placeDto.setNumber(place.getNumber());
        placeDto.setAvailable(place.isAvailable());
        return placeDto;
    }

    public static PlaceDto from(Place place, List<Rent> rents) {
        PlaceDto placeDto = from(place);
        placeDto.setRents(rents.stream().map(RentDto::from).toList());
        return placeDto;
    }
}
