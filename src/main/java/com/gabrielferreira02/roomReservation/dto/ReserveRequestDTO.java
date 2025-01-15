package com.gabrielferreira02.roomReservation.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ReserveRequestDTO {
    @NotNull
    private Long userId;

    @NotNull
    private Long roomId;

    @NotNull
    @Min(value = 1)
    private int days;
}
