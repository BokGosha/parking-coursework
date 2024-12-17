package ru.bokgosha.parking.service.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bokgosha.parking.DTO.RentDTO;
import ru.bokgosha.parking.model.Place;
import ru.bokgosha.parking.model.Rent;
import ru.bokgosha.parking.model.User;
import ru.bokgosha.parking.repository.PlaceRepository;
import ru.bokgosha.parking.repository.RentRepository;
import ru.bokgosha.parking.repository.UserRepository;
import ru.bokgosha.parking.service.RentService;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class RentServiceImplementation implements RentService {

    private final RentRepository rentRepository;
    private final PlaceRepository placeRepository;
    private final UserRepository userRepository;

    @Autowired
    public RentServiceImplementation(RentRepository rentRepository, PlaceRepository placeRepository, UserRepository userRepository) {
        this.rentRepository = rentRepository;
        this.placeRepository = placeRepository;
        this.userRepository = userRepository;
    }

    public List<RentDTO> getRents() {
        return rentRepository.findAll().stream().map(Rent::toDto).toList();
    }

    public RentDTO getRent(Long id) {
        return rentRepository.findById(id).map(Rent::toDto).orElse(null);
    }

    public RentDTO getRentByCarIdAndUserId(Long placeId, Long userId) {
        List<Rent> userRents = rentRepository.findByUser(userRepository.findById(userId).orElseThrow());

        Rent rent = userRents.stream()
                .filter(r -> r.getPlace().getId().equals(placeId))
                .findFirst().orElseThrow();

        return rent.toDto();
    }

    public RentDTO setRent(Long userId, Long carId) {
        User user = userRepository.findById(userId).orElseThrow();
        Place place = placeRepository.findById(carId).orElseThrow();

        if (place.isAvailable()) {
            Rent rent = new Rent();

            rent.setUser(user);
            place.setAvailable(false);
            rent.setPlace(place);
            rent.setStartDate(LocalDateTime.now());
            rent.setFinishDate(null);

            rentRepository.save(rent);

            return rent.toDto();
        } else {
            return null;
        }
    }

    public void finishRent(Long id) {
        Rent rent = rentRepository.findById(id).orElseThrow();
        User user = rent.getUser();

        rent.getPlace().setAvailable(true);
        rent.setFinishDate(LocalDateTime.now());
        user.getRents().add(rent);

        if (user.getTotalTime() == null) {
            user.setTotalTime(Duration.between(rent.getStartDate(), rent.getFinishDate()).toSeconds());
        } else {
            user.setTotalTime(user.getTotalTime() + Duration.between(rent.getStartDate(), rent.getFinishDate()).toSeconds());
        }
    }

    public void deleteRent(Long rentId) {
        rentRepository.deleteById(rentId);
    }

    public void deleteRents() {
        rentRepository.deleteAll();
    }

    public List<RentDTO> getFilteredRents(String filteredBy, String value) {
        var entities = switch (filteredBy) {
            case "startDate" -> rentRepository.findByStartDateEquals(LocalDate.parse(value));
            case "finishDate" -> rentRepository.findByFinishDateEquals(LocalDate.parse(value));
            case "placeId" -> rentRepository.findByPlace(placeRepository.findById(Long.valueOf(value)).orElseThrow());
            case "userId" -> rentRepository.findByUser(userRepository.findById(Long.valueOf(value)).orElseThrow());
            default -> throw new IllegalStateException("Unexpected value: " + filteredBy);
        };

        return entities.stream().map(Rent::toDto).toList();
    }
}
