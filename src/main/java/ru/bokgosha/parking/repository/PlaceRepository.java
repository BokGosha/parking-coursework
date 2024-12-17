package ru.bokgosha.parking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.bokgosha.parking.model.Place;

import java.util.List;

public interface PlaceRepository extends JpaRepository<Place, Long> {

    List<Place> findPlaceByAvailableEquals(Boolean available);
}
