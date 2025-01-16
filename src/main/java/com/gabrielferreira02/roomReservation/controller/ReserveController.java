package com.gabrielferreira02.roomReservation.controller;

import com.gabrielferreira02.roomReservation.dto.ReserveRequestDTO;
import com.gabrielferreira02.roomReservation.entity.ReserveEntity;
import com.gabrielferreira02.roomReservation.service.ReserveServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reserves")
public class ReserveController {

    private final ReserveServiceImpl reserveService;

    public ReserveController(ReserveServiceImpl reserveService) {
        this.reserveService = reserveService;
    }

    @GetMapping
    public List<ReserveEntity> findAll() {
        return reserveService.findAll();
    }

    @GetMapping("{id}")
    public ReserveEntity findById(@PathVariable Long id) {
        return reserveService.findById(id);
    }

    @GetMapping("user/{id}")
    public List<ReserveEntity> findReserveByUserId(@PathVariable Long id) {
        return reserveService.findReserveByUserId(id);
    }

    @PostMapping
    public ResponseEntity<ReserveEntity> createReserve(@Valid @RequestBody ReserveRequestDTO reserveDTO) {
        return reserveService.createReserve(reserveDTO);
    }

    @PutMapping("{id}")
    public ResponseEntity<ReserveEntity> updateReserve(@Valid @RequestBody ReserveRequestDTO reserveDTO,@PathVariable Long id) {
        return reserveService.updateReserve(reserveDTO, id);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteReserve(@PathVariable Long id) {
        return reserveService.deleteReserve(id);
    }
}
