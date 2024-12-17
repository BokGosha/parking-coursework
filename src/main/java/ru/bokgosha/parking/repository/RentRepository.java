package ru.bokgosha.parking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.bokgosha.parking.model.Place;
import ru.bokgosha.parking.model.Rent;
import ru.bokgosha.parking.model.User;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RentRepository extends JpaRepository<Rent, Long> {

    List<Rent> findByStartDateEquals(LocalDate value);
    List<Rent> findByFinishDateEquals(LocalDate value);
    List<Rent> findByPlace(Place place);
    List<Rent> findByUser(User user);
}
