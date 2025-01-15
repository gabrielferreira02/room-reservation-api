package com.gabrielferreira02.roomReservation.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class RoomRequestDTO {
    @NotNull
    private Long typeId;
}
