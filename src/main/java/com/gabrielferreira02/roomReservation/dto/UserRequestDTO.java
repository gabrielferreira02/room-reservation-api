package com.gabrielferreira02.roomReservation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.br.CPF;

@Getter
@AllArgsConstructor
public class UserRequestDTO {
    @NotBlank(message = "O nome não pode estar em branco.")
    private String name;

    @Size(min = 8, message = "A senha deve conter no mínimo 8 caracteres.")
    private String password;

    @CPF(message = "CPF inválido.")
    private String cpf;
}
