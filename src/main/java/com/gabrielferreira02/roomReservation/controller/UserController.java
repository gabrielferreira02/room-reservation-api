package com.gabrielferreira02.roomReservation.controller;

import com.gabrielferreira02.roomReservation.dto.JwtResponseDTO;
import com.gabrielferreira02.roomReservation.dto.LoginDTO;
import com.gabrielferreira02.roomReservation.dto.UserRequestDTO;
import com.gabrielferreira02.roomReservation.entity.UserEntity;
import com.gabrielferreira02.roomReservation.service.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@Tag(name = "User's controller", description = "Simple crud for users")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @PreAuthorize("permitAll()")
    @GetMapping
    @Operation(
            summary = "List users",
            description = "Return a list with all users",
            tags = "User's controller",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Request return successfully"
                    )
            }
    )
    public List<UserEntity> findAll() {
        return userService.findAll();
    }

    @PreAuthorize("permitAll()")
    @PostMapping
    @Operation(
            summary = "Create user",
            description = "Create a new user",
            tags = "User's controller",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User request with name, cpf and password",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserRequestDTO.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Success on create an user",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserEntity.class)
                            )
                    )
            }
    )
    public ResponseEntity<UserEntity> createUser(@RequestBody @Valid UserRequestDTO userDTO) {
        return userService.createUser(userDTO);
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("{id}")
    @Operation(
            summary = "Update user",
            description = "Update user's attributes",
            tags = "User's controller",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User request with name, cpf and password",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserRequestDTO.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Success on update an user",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserEntity.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access denied to delete a user",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<UserEntity> updateUser(@RequestBody @Valid UserRequestDTO userDTO, @PathVariable Long id) {
        return userService.updateUser(userDTO, id);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @DeleteMapping("{id}")
    @Operation(
            summary = "Delete user",
            description = "Delete an existent user",
            tags = "User's controller",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Success on delete an user",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access denied to delete a user",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }
}
