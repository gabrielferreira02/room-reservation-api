package com.gabrielferreira02.roomReservation.service;

import com.gabrielferreira02.roomReservation.dto.TypeRequestDTO;
import com.gabrielferreira02.roomReservation.entity.TypeEntity;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TypeService {
    List<TypeEntity> findAll();
    ResponseEntity<TypeEntity> createType(TypeRequestDTO typeDTO);
    ResponseEntity<TypeEntity> updateType(TypeRequestDTO typeDTO, Long id);
    ResponseEntity<?> deleteType(Long id);
}
