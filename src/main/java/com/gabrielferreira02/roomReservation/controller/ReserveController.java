package com.gabrielferreira02.roomReservation.controller;

import com.gabrielferreira02.roomReservation.dto.ReserveRequestDTO;
import com.gabrielferreira02.roomReservation.entity.ReserveEntity;
import com.gabrielferreira02.roomReservation.service.ReserveServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reserves")
public class ReserveController {

    private final ReserveServiceImpl reserveService;

    public ReserveController(ReserveServiceImpl reserveService) {
        this.reserveService = reserveService;
    }

    @PreAuthorize("permitAll()")
    @GetMapping
    public List<ReserveEntity> findAll() {
        return reserveService.findAll();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("{id}")
    public ReserveEntity findById(@PathVariable Long id) {
        return reserveService.findById(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("user/{id}")
    public List<ReserveEntity> findReserveByUserId(@PathVariable Long id) {
        return reserveService.findReserveByUserId(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping
    public ResponseEntity<ReserveEntity> createReserve(@Valid @RequestBody ReserveRequestDTO reserveDTO) {
        return reserveService.createReserve(reserveDTO);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PutMapping("{id}")
    public ResponseEntity<ReserveEntity> updateReserve(@Valid @RequestBody ReserveRequestDTO reserveDTO,@PathVariable Long id) {
        return reserveService.updateReserve(reserveDTO, id);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteReserve(@PathVariable Long id) {
        return reserveService.deleteReserve(id);
    }
}
