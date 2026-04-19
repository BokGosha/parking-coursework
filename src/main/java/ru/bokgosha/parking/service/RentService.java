package ru.bokgosha.parking.service;

import ru.bokgosha.parking.dto.RentDto;

import java.util.List;

public interface RentService {

    List<RentDto> getRents();

    RentDto getRentById(Long id);

    RentDto createRent(Long userId, Long placeId);

    void finishRent(Long id);

    void deleteRent(Long rentId);

    void deleteRents();

    RentDto getRentByCarIdAndUserId(Long carId, Long userId);

    List<RentDto> getFilteredRents(String filteredBy, String value);
}
