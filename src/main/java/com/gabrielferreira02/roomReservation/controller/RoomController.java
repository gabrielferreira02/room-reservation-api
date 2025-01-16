package com.gabrielferreira02.roomReservation.controller;

import com.gabrielferreira02.roomReservation.dto.RoomRequestDTO;
import com.gabrielferreira02.roomReservation.entity.RoomEntity;
import com.gabrielferreira02.roomReservation.service.RoomServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    private final RoomServiceImpl roomService;

    public RoomController(RoomServiceImpl roomService) {
        this.roomService = roomService;
    }

    @PreAuthorize("permitAll()")
    @GetMapping
    public List<RoomEntity> findAll() {
        return roomService.findAll();
    }

    @PreAuthorize("permitAll()")
    @GetMapping("free")
    public List<RoomEntity> findAllFreeRooms() {
        return roomService.findAllFreeRooms();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<RoomEntity> createRoom(@RequestBody @Valid RoomRequestDTO roomDTO) {
        return roomService.createRoom(roomDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("{id}")
    public ResponseEntity<RoomEntity> updateRoom(@RequestBody @Valid RoomRequestDTO roomDTO, @PathVariable Long id) {
        return roomService.updateRoom(roomDTO, id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteRoom(@PathVariable Long id) {
        return roomService.deleteRoom(id);
    }
}
