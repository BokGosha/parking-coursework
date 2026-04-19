package ru.bokgosha.parking.dto;

import lombok.Data;
import ru.bokgosha.parking.model.Rent;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class RentDto {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private Long id;
    private Long userId;
    private String username;
    private Long placeId;
    private Integer placeNumber;
    private LocalDateTime startDate;
    private LocalDateTime finishDate;

    public static RentDto from(Rent rent) {
        RentDto rentDto = new RentDto();
        rentDto.setId(rent.getId());
        rentDto.setStartDate(rent.getStartDate());
        rentDto.setFinishDate(rent.getFinishDate());
        if (rent.getUser() != null) {
            rentDto.setUserId(rent.getUser().getId());
            rentDto.setUsername(rent.getUser().getUsername());
        }
        if (rent.getPlace() != null) {
            rentDto.setPlaceId(rent.getPlace().getId());
            rentDto.setPlaceNumber(rent.getPlace().getNumber());
        }
        return rentDto;
    }

    public String getFormattedStartDate() {
        return startDate != null ? startDate.format(DATE_FORMAT) : null;
    }

    public String getFormattedFinishDate() {
        return finishDate != null ? finishDate.format(DATE_FORMAT) : null;
    }

    public long getDurationInSeconds() {
        if (startDate == null) {
            return 0;
        }

        LocalDateTime end = finishDate != null ? finishDate : LocalDateTime.now();
        return Duration.between(startDate, end).getSeconds();
    }
}
