package ru.bokgosha.parking.service;

import ru.bokgosha.parking.dto.PlaceDto;
import ru.bokgosha.parking.model.Place;

import java.util.List;

public interface PlaceService {

    List<PlaceDto> getPlaces();

    List<PlaceDto> getPlacesSortedByNumber();

    List<PlaceDto> getFilteredPlacesByAvailable(boolean available);

    Place getPlaceById(Long id);

    PlaceDto getPlaceDetails(Long id);
}
