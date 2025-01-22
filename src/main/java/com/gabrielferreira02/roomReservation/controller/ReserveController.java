package com.gabrielferreira02.roomReservation.controller;

import com.gabrielferreira02.roomReservation.dto.ReserveRequestDTO;
import com.gabrielferreira02.roomReservation.entity.ReserveEntity;
import com.gabrielferreira02.roomReservation.service.ReserveServiceImpl;
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
@RequestMapping("/reserves")
@Tag(name = "Reserve's controller", description = "Simple crud for reserves")
public class ReserveController {

    private final ReserveServiceImpl reserveService;

    public ReserveController(ReserveServiceImpl reserveService) {
        this.reserveService = reserveService;
    }

    @PreAuthorize("permitAll()")
    @GetMapping
    @Operation(
            summary = "List reserves",
            description = "It Returns a list of all reserves",
            tags = "Reserve's controller",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Request executed successfully"
                    )
            }
    )
    public List<ReserveEntity> findAll() {
        return reserveService.findAll();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("{id}")
    @Operation(
            summary = "Return reserve",
            description = "It Returns a specific reserve",
            tags = "Reserve's controller",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Request executed successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ReserveEntity.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access denied",
                            content = @Content
                    )
            }
    )
    public ReserveEntity findById(@PathVariable Long id) {
        return reserveService.findById(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("user/{id}")
    @Operation(
            summary = "Return reserves of an user",
            description = "It Returns all reserve of an user",
            tags = "Reserve's controller",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Request executed successfully"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access denied",
                            content = @Content
                    )
            }
    )
    public List<ReserveEntity> findReserveByUserId(@PathVariable Long id) {
        return reserveService.findReserveByUserId(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping
    @Operation(
            summary = "Create reserve",
            description = "It creates a reserve",
            tags = "Reserve's controller",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Reserve created successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ReserveEntity.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access denied",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<ReserveEntity> createReserve(@Valid @RequestBody ReserveRequestDTO reserveDTO) {
        return reserveService.createReserve(reserveDTO);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PutMapping("{id}")
    @Operation(
            summary = "Update reserve",
            description = "It updates a reserve",
            tags = "Reserve's controller",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Reserve updated successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ReserveEntity.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access denied",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<ReserveEntity> updateReserve(@Valid @RequestBody ReserveRequestDTO reserveDTO,@PathVariable Long id) {
        return reserveService.updateReserve(reserveDTO, id);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @DeleteMapping("{id}")
    @Operation(
            summary = "Delete reserve",
            description = "It deletes a reserve",
            tags = "Reserve's controller",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Reserve deleted successfully",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access denied",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<?> deleteReserve(@PathVariable Long id) {
        return reserveService.deleteReserve(id);
    }
}
