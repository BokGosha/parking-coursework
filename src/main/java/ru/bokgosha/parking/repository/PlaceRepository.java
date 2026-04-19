package ru.bokgosha.parking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.bokgosha.parking.model.Place;

import java.util.List;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {

    List<Place> findPlacesByAvailable(boolean available);

    List<Place> findPlacesByOrderByNumberAsc();
}
