package com.gabrielferreira02.roomReservation.controller;

import com.gabrielferreira02.roomReservation.dto.TypeRequestDTO;
import com.gabrielferreira02.roomReservation.entity.RoomEntity;
import com.gabrielferreira02.roomReservation.entity.TypeEntity;
import com.gabrielferreira02.roomReservation.service.TypeServiceImpl;
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
@RequestMapping("/room-types")
@Tag(name = "Type's controller", description = "Simple crud for room types")
public class TypeController {

    private final TypeServiceImpl typeService;

    public TypeController(TypeServiceImpl typeService) {
        this.typeService = typeService;
    }

    @PreAuthorize("permitAll()")
    @GetMapping
    @Operation(
            summary = "List types",
            description = "It Returns a list of all types",
            tags = "Type's controller",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Success on request"
                    )
            }
    )
    public List<TypeEntity> findAll() {
        return typeService.findAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @Operation(
            summary = "Create type",
            description = "Create a type",
            tags = "Type's controller",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Type created successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TypeEntity.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access denied to create a type",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<TypeEntity> createType(@RequestBody @Valid TypeRequestDTO typeDTO) {
        return typeService.createType(typeDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("{id}")
    @Operation(
            summary = "Update type",
            description = "Update a type",
            tags = "Type's controller",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Type updated successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TypeEntity.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access denied to update a type",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<TypeEntity> updateType(@RequestBody @Valid TypeRequestDTO typeDTO, @PathVariable Long id) {
        return typeService.updateType(typeDTO, id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("{id}")
    @Operation(
            summary = "Delete type",
            description = "Delete a type",
            tags = "Type's controller",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Type deleted successfully",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access denied to delete a type",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<?> deleteType(@PathVariable Long id) {
        return typeService.deleteType(id);
    }
}
