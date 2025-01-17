package com.gabrielferreira02.roomReservation.service;

import com.gabrielferreira02.roomReservation.dto.LoginDTO;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<?> login(LoginDTO loginDTO);
}
