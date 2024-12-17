package ru.bokgosha.parking.service.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bokgosha.parking.DTO.PlaceDTO;
import ru.bokgosha.parking.model.Place;
import ru.bokgosha.parking.repository.PlaceRepository;
import ru.bokgosha.parking.service.PlaceService;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PlaceServiceImplementation implements PlaceService {

    private final PlaceRepository PlaceRepository;

    public List<PlaceDTO> getPlaces() {
        return PlaceRepository.findAll().stream().map(Place::toDto).toList();
    }

    public PlaceDTO getPlace(Long id) {
        return PlaceRepository.findById(id).map(Place::toDto).orElse(null);
    }

    public List<PlaceDTO> getPlacesSortedByNumber() {
        return PlaceRepository.findAll(Sort.by(Sort.Direction.ASC, "number"))
                .stream().map(Place::toDto).toList();
    }

    public List<PlaceDTO> getFilteredPlacesByAvailable(String value) {
        boolean val = value.equals("Доступно");

        var entities = PlaceRepository.findPlaceByAvailableEquals(val);

        return entities.stream().map(Place::toDto).toList();
    }
}
