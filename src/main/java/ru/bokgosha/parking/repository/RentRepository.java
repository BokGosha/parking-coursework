package ru.bokgosha.parking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.bokgosha.parking.model.Place;
import ru.bokgosha.parking.model.Rent;
import ru.bokgosha.parking.model.User;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RentRepository extends JpaRepository<Rent, Long> {

    List<Rent> findRentsByStartDate(LocalDate value);

    List<Rent> findRentsByFinishDate(LocalDate value);

    List<Rent> findRentsByPlace(Place place);

    List<Rent> findRentsByUser(User user);

    @Query("""
            SELECT r FROM Rent r
            JOIN FETCH r.place
            WHERE r.user.username = :username
            ORDER BY r.startDate DESC
            """)
    List<Rent> findRentsByUsernameWithPlace(@Param("username") String username);

    @Query("""
            SELECT r FROM Rent r
            JOIN FETCH r.user
            WHERE r.place.id = :placeId
            ORDER BY r.startDate DESC
            """)
    List<Rent> findRentsByPlaceIdWithUser(@Param("placeId") Long placeId);
}
