package com.gabrielferreira02.roomReservation.service;

import com.gabrielferreira02.roomReservation.dto.UserRequestDTO;
import com.gabrielferreira02.roomReservation.entity.UserEntity;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {
    List<UserEntity> findAll();
    ResponseEntity<UserEntity> createUser(UserRequestDTO userDTO);
    ResponseEntity<UserEntity> updateUser(UserRequestDTO userDTO, Long id);
    ResponseEntity<?> deleteUser(Long id);
}
