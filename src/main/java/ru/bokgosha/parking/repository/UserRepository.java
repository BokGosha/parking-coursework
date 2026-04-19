package ru.bokgosha.parking.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.bokgosha.parking.dto.TopUserDto;
import ru.bokgosha.parking.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByUsername(String username);

    @Query("""
            SELECT new ru.bokgosha.parking.dto.TopUserDto(u.username, COUNT(r), u.totalTime)
            FROM User u LEFT JOIN Rent r ON r.user = u
            GROUP BY u.id, u.username, u.totalTime
            ORDER BY COUNT(r) DESC
            """)
    List<TopUserDto> findTopUsers(Pageable pageable);
}
