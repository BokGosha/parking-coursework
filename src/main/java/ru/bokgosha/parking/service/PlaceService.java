package ru.bokgosha.parking.service;

import ru.bokgosha.parking.DTO.PlaceDTO;

import java.util.List;

public interface PlaceService {
    
    List<PlaceDTO> getPlaces();
    PlaceDTO getPlace(Long id);
    List<PlaceDTO> getPlacesSortedByNumber();
    List<PlaceDTO> getFilteredPlacesByAvailable(String value);
}
