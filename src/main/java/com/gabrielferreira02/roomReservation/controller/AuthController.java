package com.gabrielferreira02.roomReservation.controller;

import com.gabrielferreira02.roomReservation.dto.JwtResponseDTO;
import com.gabrielferreira02.roomReservation.dto.LoginDTO;
import com.gabrielferreira02.roomReservation.service.AuthServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@Tag(name = "Authentication", description = "Route for login")
public class AuthController {
    @Autowired
    private AuthServiceImpl authService;

    @PostMapping("login")
    @PreAuthorize("permitAll()")
    @Operation(
            summary = "Login user",
            description = "Authenticate a user and return a token jwt",
            tags = "Authentication",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Authentication request with username and password",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LoginDTO.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful authentication",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = JwtResponseDTO.class)
                            )
                    )
            }
    )
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        return authService.login(loginDTO);
    }
}
