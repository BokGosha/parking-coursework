package ru.bokgosha.parking.service.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bokgosha.parking.dto.PlaceDto;
import ru.bokgosha.parking.model.Place;
import ru.bokgosha.parking.model.Rent;
import ru.bokgosha.parking.repository.PlaceRepository;
import ru.bokgosha.parking.repository.RentRepository;
import ru.bokgosha.parking.service.PlaceService;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PlaceServiceImplementation implements PlaceService {

    private final PlaceRepository placeRepository;
    private final RentRepository rentRepository;

    public List<PlaceDto> getPlaces() {
        return placeRepository.findAll()
                .stream()
                .map(PlaceDto::from)
                .toList();
    }

    public Place getPlaceById(Long id) {
        return placeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Место c id=" + id + " не найдено"));
    }

    public List<PlaceDto> getPlacesSortedByNumber() {
        return placeRepository.findPlacesByOrderByNumberAsc()
                .stream()
                .map(PlaceDto::from)
                .toList();
    }

    public List<PlaceDto> getFilteredPlacesByAvailable(boolean available) {
        return placeRepository.findPlacesByAvailable(available)
                .stream()
                .map(PlaceDto::from)
                .toList();
    }

    public PlaceDto getPlaceDetails(Long id) {
        Place place = getPlaceById(id);
        List<Rent> rents = rentRepository.findRentsByPlaceIdWithUser(id);
        return PlaceDto.from(place, rents);
    }
}
