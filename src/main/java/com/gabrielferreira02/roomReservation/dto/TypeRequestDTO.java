package com.gabrielferreira02.roomReservation.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TypeRequestDTO {
    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotNull
    @Min(value = 100)
    private double price;
}
