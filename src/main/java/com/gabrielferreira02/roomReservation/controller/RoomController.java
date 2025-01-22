package com.gabrielferreira02.roomReservation.controller;

import com.gabrielferreira02.roomReservation.dto.RoomRequestDTO;
import com.gabrielferreira02.roomReservation.entity.RoomEntity;
import com.gabrielferreira02.roomReservation.service.RoomServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rooms")
@Tag(name = "Room's controller", description = "Simple crud for rooms")
public class RoomController {

    private final RoomServiceImpl roomService;

    public RoomController(RoomServiceImpl roomService) {
        this.roomService = roomService;
    }

    @PreAuthorize("permitAll()")
    @GetMapping
    @Operation(
            summary = "List rooms",
            description = "Return a list with all rooms",
            tags = "Room's controller",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Request return successfully"
                    )
            }
    )
    public List<RoomEntity> findAll() {
        return roomService.findAll();
    }

    @PreAuthorize("permitAll()")
    @GetMapping("free")
    @Operation(
            summary = "List free rooms",
            description = "Return a list with all free rooms",
            tags = "Room's controller",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Request return successfully"
                    )
            }
    )
    public List<RoomEntity> findAllFreeRooms() {
        return roomService.findAllFreeRooms();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @Operation(
            summary = "Create room",
            description = "Create a new room",
            tags = "Room's controller",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Room created successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = RoomEntity.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access denied to create a room",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<RoomEntity> createRoom(@RequestBody @Valid RoomRequestDTO roomDTO) {
        return roomService.createRoom(roomDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("{id}")
    @Operation(
            summary = "Update room",
            description = "Update a room",
            tags = "Room's controller",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Room updated successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = RoomEntity.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access denied to update a room",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<RoomEntity> updateRoom(@RequestBody @Valid RoomRequestDTO roomDTO, @PathVariable Long id) {
        return roomService.updateRoom(roomDTO, id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("{id}")
    @Operation(
            summary = "Delete room",
            description = "Delete a room",
            tags = "Room's controller",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Room deleted successfully",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access denied to delete a room",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<?> deleteRoom(@PathVariable Long id) {
        return roomService.deleteRoom(id);
    }
}
