package com.gabrielferreira02.roomReservation.controller;

import com.gabrielferreira02.roomReservation.dto.UserRequestDTO;
import com.gabrielferreira02.roomReservation.entity.UserEntity;
import com.gabrielferreira02.roomReservation.service.UserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @GetMapping
    public List<UserEntity> findAll() {
        return userService.findAll();
    }

    @PostMapping
    public ResponseEntity<UserEntity> createUser(@RequestBody @Valid UserRequestDTO userDTO) {
        return userService.createUser(userDTO);
    }

    @PutMapping("{id}")
    public ResponseEntity<UserEntity> updateUser(@RequestBody @Valid UserRequestDTO userDTO, @PathVariable Long id) {
        return userService.updateUser(userDTO, id);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }
}
