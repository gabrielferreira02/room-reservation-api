package com.gabrielferreira02.roomReservation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class LoginDTO {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
