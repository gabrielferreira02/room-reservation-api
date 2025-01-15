package com.gabrielferreira02.roomReservation.controller;

import com.gabrielferreira02.roomReservation.dto.TypeRequestDTO;
import com.gabrielferreira02.roomReservation.entity.TypeEntity;
import com.gabrielferreira02.roomReservation.service.TypeServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/room-types")
public class TypeController {

    private final TypeServiceImpl typeService;

    public TypeController(TypeServiceImpl typeService) {
        this.typeService = typeService;
    }

    @GetMapping
    public List<TypeEntity> findAll() {
        return typeService.findAll();
    }

    @PostMapping
    public ResponseEntity<TypeEntity> createUser(@RequestBody @Valid TypeRequestDTO typeDTO) {
        return typeService.createType(typeDTO);
    }

    @PutMapping("{id}")
    public ResponseEntity<TypeEntity> updateUser(@RequestBody @Valid TypeRequestDTO typeDTO, @PathVariable Long id) {
        return typeService.updateType(typeDTO, id);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        return typeService.deleteType(id);
    }
}
