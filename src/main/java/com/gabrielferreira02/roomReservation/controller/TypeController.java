package com.gabrielferreira02.roomReservation.controller;

import com.gabrielferreira02.roomReservation.dto.TypeRequestDTO;
import com.gabrielferreira02.roomReservation.entity.TypeEntity;
import com.gabrielferreira02.roomReservation.service.TypeServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/room-types")
public class TypeController {

    private final TypeServiceImpl typeService;

    public TypeController(TypeServiceImpl typeService) {
        this.typeService = typeService;
    }

    @PreAuthorize("permitAll()")
    @GetMapping
    public List<TypeEntity> findAll() {
        return typeService.findAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<TypeEntity> createType(@RequestBody @Valid TypeRequestDTO typeDTO) {
        return typeService.createType(typeDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("{id}")
    public ResponseEntity<TypeEntity> updateType(@RequestBody @Valid TypeRequestDTO typeDTO, @PathVariable Long id) {
        return typeService.updateType(typeDTO, id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteType(@PathVariable Long id) {
        return typeService.deleteType(id);
    }
}
