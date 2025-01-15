package com.gabrielferreira02.roomReservation.controller;

import com.gabrielferreira02.roomReservation.dto.RoomRequestDTO;
import com.gabrielferreira02.roomReservation.entity.RoomEntity;
import com.gabrielferreira02.roomReservation.service.RoomServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    private final RoomServiceImpl roomService;

    public RoomController(RoomServiceImpl roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    public List<RoomEntity> findAll() {
        return roomService.findAll();
    }

    @GetMapping("free")
    public List<RoomEntity> findAllFreeRooms() {
        return roomService.findAllFreeRooms();
    }

    @PostMapping
    public ResponseEntity<RoomEntity> createRoom(@RequestBody @Valid RoomRequestDTO roomDTO) {
        return roomService.createRoom(roomDTO);
    }

    @PutMapping("{id}")
    public ResponseEntity<RoomEntity> updateRoom(@RequestBody @Valid RoomRequestDTO roomDTO, @PathVariable Long id) {
        return roomService.updateRoom(roomDTO, id);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteRoom(@PathVariable Long id) {
        return roomService.deleteRoom(id);
    }
}
