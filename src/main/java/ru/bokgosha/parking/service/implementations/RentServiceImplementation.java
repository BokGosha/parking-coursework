package ru.bokgosha.parking.service.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bokgosha.parking.dto.RentDto;
import ru.bokgosha.parking.model.Place;
import ru.bokgosha.parking.model.Rent;
import ru.bokgosha.parking.model.User;
import ru.bokgosha.parking.repository.RentRepository;
import ru.bokgosha.parking.service.PlaceService;
import ru.bokgosha.parking.service.RentService;
import ru.bokgosha.parking.service.UserService;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RentServiceImplementation implements RentService {

    private final RentRepository rentRepository;
    private final PlaceService placeService;
    private final UserService userService;

    public List<RentDto> getRents() {
        return rentRepository.findAll()
                .stream()
                .map(RentDto::from)
                .toList();
    }

    public RentDto getRentById(Long id) {
        return rentRepository.findById(id)
                .map(RentDto::from)
                .orElseThrow(() -> new RuntimeException("Аренда c id=" + id + " не найдена"));
    }

    public RentDto getRentByCarIdAndUserId(Long placeId, Long userId) {
        List<Rent> userRents = rentRepository.findRentsByUser(userService.getUserById(userId));

        Rent rent = userRents.stream()
                .filter(r -> r.getPlace().getId().equals(placeId) && r.getFinishDate() == null)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Активная аренда с userId=" + userId + " и placeId=" + placeId + " не найдена"));

        return RentDto.from(rent);
    }

    @Transactional
    public RentDto createRent(Long userId, Long placeId) {
        User user = userService.getUserById(userId);
        Place place = placeService.getPlaceById(placeId);

        if (!place.isAvailable()) {
            throw new IllegalStateException("Место с id=" + placeId + " уже занято");
        }

        Rent rent = new Rent();
        rent.setUser(user);
        rent.setPlace(place);
        rent.setStartDate(LocalDateTime.now());
        place.setAvailable(false);

        return RentDto.from(rentRepository.save(rent));
    }

    @Transactional
    public void finishRent(Long id) {
        Rent rent = rentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Аренда c id=" + id + " не найдена"));

        if (rent.getFinishDate() != null) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        rent.setFinishDate(now);
        rent.getPlace().setAvailable(true);

        User user = rent.getUser();
        long duration = Duration.between(rent.getStartDate(), now).toSeconds();
        user.setTotalTime(user.getTotalTime() + duration);
    }

    @Transactional
    public void deleteRent(Long rentId) {
        rentRepository.deleteById(rentId);
    }

    @Transactional
    public void deleteRents() {
        rentRepository.deleteAll();
    }

    public List<RentDto> getFilteredRents(String filteredBy, String value) {
        List<Rent> entities = switch (filteredBy) {
            case "startDate" -> rentRepository.findRentsByStartDate(LocalDate.parse(value));
            case "finishDate" -> rentRepository.findRentsByFinishDate(LocalDate.parse(value));
            case "placeId" -> rentRepository.findRentsByPlace(placeService.getPlaceById(Long.valueOf(value)));
            case "userId" -> rentRepository.findRentsByUser(userService.getUserById(Long.valueOf(value)));
            default -> throw new IllegalStateException("Unexpected value: " + filteredBy);
        };

        return entities.stream()
                .map(RentDto::from)
                .toList();
    }
}
