package com.gabrielferreira02.roomReservation.controller;

import com.gabrielferreira02.roomReservation.dto.LoginDTO;
import com.gabrielferreira02.roomReservation.service.AuthServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class AuthController {
    @Autowired
    private AuthServiceImpl authService;

    @PostMapping("login")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        return authService.login(loginDTO);
    }
}
