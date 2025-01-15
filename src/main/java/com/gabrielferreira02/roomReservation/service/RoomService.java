package com.gabrielferreira02.roomReservation.service;

import com.gabrielferreira02.roomReservation.dto.RoomRequestDTO;
import com.gabrielferreira02.roomReservation.entity.RoomEntity;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface RoomService {
    List<RoomEntity> findAll();
    List<RoomEntity> findAllFreeRooms();
    ResponseEntity<RoomEntity> createRoom(RoomRequestDTO roomDTO);
    ResponseEntity<RoomEntity> updateRoom(RoomRequestDTO roomDTO, Long id);
    ResponseEntity<?> deleteRoom(Long id);
}
