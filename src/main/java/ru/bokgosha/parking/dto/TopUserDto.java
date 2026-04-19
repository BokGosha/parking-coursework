package ru.bokgosha.parking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TopUserDto {

    private String username;
    private Long rentsCount;
    private Long totalTime;
}
