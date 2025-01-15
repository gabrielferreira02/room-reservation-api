package com.gabrielferreira02.roomReservation.service;

import com.gabrielferreira02.roomReservation.dto.ReserveRequestDTO;
import com.gabrielferreira02.roomReservation.dto.RoomRequestDTO;
import com.gabrielferreira02.roomReservation.entity.ReserveEntity;
import com.gabrielferreira02.roomReservation.entity.RoomEntity;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ReserveService {
    List<ReserveEntity> findAll();
    ResponseEntity<ReserveEntity> createReserve(ReserveRequestDTO reserveDTO);
    ResponseEntity<ReserveEntity> updateReserve(ReserveRequestDTO reserveDTO, Long id);
    ResponseEntity<?> deleteReserve(Long id);
}
