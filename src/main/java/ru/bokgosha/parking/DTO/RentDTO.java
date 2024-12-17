package ru.bokgosha.parking.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RentDTO {

    private Long id;
    private Long userId;
    private Long placeId;
    private LocalDateTime startDate;
    private LocalDateTime finishDate;
}
