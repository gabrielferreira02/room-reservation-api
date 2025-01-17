package com.gabrielferreira02.roomReservation.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RoomRequestDTO {
    @NotNull
    private Long typeId;
}
