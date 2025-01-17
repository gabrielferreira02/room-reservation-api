package com.gabrielferreira02.roomReservation.service;

import com.gabrielferreira02.roomReservation.dto.TypeRequestDTO;
import com.gabrielferreira02.roomReservation.entity.TypeEntity;
import com.gabrielferreira02.roomReservation.repository.TypeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TypeServiceImplTest {

    @Mock
    private TypeRepository typeRepository;
    @InjectMocks
    private TypeServiceImpl typeService;

    private final TypeEntity type = new TypeEntity(
            1L,
            "Basic room",
            "Basic room with a single bed",
            120
    );

    @Test
    @DisplayName("It should return a list with all types")
    void findAll() {
        when(typeRepository.findAll()).thenReturn(List.of(type));

        List<TypeEntity> response = typeService.findAll();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("Basic room", response.getFirst().getName());
        assertEquals(120, response.getFirst().getPrice());
        assertEquals("Basic room with a single bed", response.getFirst().getDescription());
    }

    @Test
    @DisplayName("it should create a type successfully")
    void createType() {
        TypeRequestDTO request = new TypeRequestDTO(
                "Basic room",
                "Basic room with a single bed",
                120
        );

        when(typeRepository.save(any(TypeEntity.class))).thenReturn(type);

        ResponseEntity<?> response = typeService.createType(request);

        TypeEntity body = (TypeEntity) response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response);
        assertNotNull(body);
        assertEquals(request.getName(), body.getName());
        assertEquals(request.getDescription(), body.getDescription());
        assertEquals(request.getPrice(), body.getPrice());

    }

    @Test
    @DisplayName("it should fail on create a type due to name field is blank")
    void createTypeErrorCase1() {
        TypeRequestDTO request = new TypeRequestDTO(
                "",
                "Basic room with a single bed",
                120
        );

        when(typeRepository.save(any(TypeEntity.class))).thenThrow(IllegalArgumentException.class);

        assertThrows(IllegalArgumentException.class, () -> {
            typeService.createType(request);
        });

    }

    @Test
    @DisplayName("it should fail on create a type due to description field is blank")
    void createTypeErrorCase2() {
        TypeRequestDTO request = new TypeRequestDTO(
                "Basic room",
                "",
                120
        );

        when(typeRepository.save(any(TypeEntity.class))).thenThrow(IllegalArgumentException.class);

        assertThrows(IllegalArgumentException.class, () -> {
            typeService.createType(request);
        });

    }

    @Test
    @DisplayName("It should update a user successfully")
    void updateType() {
        TypeRequestDTO request = new TypeRequestDTO(
                "Basic room",
                "Basic room with a single bed",
                100
        );

        type.setDescription(request.getDescription());
        type.setName(request.getName());
        type.setPrice(request.getPrice());

        when(typeRepository.save(any(TypeEntity.class))).thenReturn(type);

        ResponseEntity<?> response = typeService.createType(request);

        TypeEntity body = (TypeEntity) response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response);
        assertNotNull(body);
        assertEquals(request.getName(), body.getName());
        assertEquals(request.getDescription(), body.getDescription());
        assertEquals(request.getPrice(), body.getPrice());
    }

    @Test
    @DisplayName("it should fail on create a type due to description field is blank")
    void updateTypeErrorCase1() {
        TypeRequestDTO request = new TypeRequestDTO(
                "Basic room",
                "",
                120
        );

        when(typeRepository.save(any(TypeEntity.class))).thenThrow(IllegalArgumentException.class);

        assertThrows(IllegalArgumentException.class, () -> {
            typeService.createType(request);
        });

    }

    @Test
    @DisplayName("it should fail on create a type due to name field is blank")
    void updateTypeErrorCase2() {
        TypeRequestDTO request = new TypeRequestDTO(
                "",
                "Basic room with a single bed",
                120
        );

        when(typeRepository.save(any(TypeEntity.class))).thenThrow(IllegalArgumentException.class);

        assertThrows(IllegalArgumentException.class, () -> {
            typeService.createType(request);
        });

    }

    @Test
    @DisplayName("It should delete a type successfully")
    void deleteType() {
        when(typeRepository.existsById(any(Long.class))).thenReturn(true);

        ResponseEntity<?> response = typeService.deleteType(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    @DisplayName("It should fail on delete a user with no existent id")
    void deleteTypeError() {
        when(typeRepository.existsById(any(Long.class))).thenReturn(false);

        ResponseEntity<?> response = typeService.deleteType(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}