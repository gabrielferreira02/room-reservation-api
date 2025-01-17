package com.gabrielferreira02.roomReservation.service;

import com.gabrielferreira02.roomReservation.dto.RoomRequestDTO;
import com.gabrielferreira02.roomReservation.entity.ReserveEntity;
import com.gabrielferreira02.roomReservation.entity.RoomEntity;
import com.gabrielferreira02.roomReservation.entity.TypeEntity;
import com.gabrielferreira02.roomReservation.repository.RoomRepository;
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
class RoomServiceImplTest {

    @Mock
    private RoomRepository roomRepository;
    @Mock
    private TypeRepository typeRepository;
    @InjectMocks
    private RoomServiceImpl roomService;

    private final TypeEntity type = new TypeEntity(
            1L,
            "Basic room",
            "Some description",
            100
    );

    private final RoomEntity room = new RoomEntity(
            1L,
            type,
            List.of(new ReserveEntity())
    );

    @Test
    @DisplayName("It should return a list with all rooms")
    void findAll() {
        when(roomRepository.findAll()).thenReturn(List.of(room));

        List<RoomEntity> response = roomService.findAll();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(room.getRoomType(), response.getFirst().getRoomType());
    }

    @Test
    @DisplayName("It should return a list with all rooms not reserved")
    void findAllFreeRooms() {
        when(roomRepository.findAllFreeRooms()).thenReturn(List.of());

        List<RoomEntity> response = roomService.findAllFreeRooms();

        assertNotNull(response);
        assertEquals(0, response.size());
    }

    @Test
    @DisplayName("It should create a room successfully")
    void createRoom() {
        RoomRequestDTO request = new RoomRequestDTO(1L);

        when(typeRepository.findById(anyLong())).thenReturn(Optional.of(type));
        when(roomRepository.save(any(RoomEntity.class))).thenReturn(room);

        ResponseEntity<?> response = roomService.createRoom(request);

        RoomEntity body = (RoomEntity) response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response);
        assertNotNull(body);
        assertEquals(room.getRoomType(), body.getRoomType());
    }

    @Test
    @DisplayName("It should fail on create a room")
    void createRoomError() {
        RoomRequestDTO request = new RoomRequestDTO(1L);

        when(typeRepository.findById(anyLong())).thenThrow(RuntimeException.class);

        assertThrows(RuntimeException.class, () -> {
            roomService.createRoom(request);
        });
    }

    @Test
    @DisplayName("It should update a room successfully")
    void updateRoom() {
        RoomRequestDTO request = new RoomRequestDTO(2L);

        TypeEntity secondType = new TypeEntity(
                2L,
                "Twin room",
                "Some description",
                150
        );

        when(roomRepository.findById(anyLong())).thenReturn(Optional.of(room));
        when(typeRepository.findById(anyLong())).thenReturn(Optional.of(secondType));
        room.setRoomType(secondType);
        when(roomRepository.save(any(RoomEntity.class))).thenReturn(room);

        ResponseEntity<?> response = roomService.updateRoom(request, 1L);

        RoomEntity body = (RoomEntity) response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response);
        assertNotNull(body);
        assertEquals(secondType, body.getRoomType());
    }

    @Test
    @DisplayName("It should fail on update due to no existent type id")
    void updateRoomErrorCase1() {
        RoomRequestDTO request = new RoomRequestDTO(2L);

        when(roomRepository.findById(anyLong())).thenReturn(Optional.of(room));
        when(typeRepository.findById(anyLong())).thenThrow(RuntimeException.class);

        assertThrows(RuntimeException.class, () -> {
            roomService.updateRoom(request, 1L);
        });
    }

    @Test
    @DisplayName("It should fail on update due to no existent room id")
    void updateRoomErrorCase2() {
        RoomRequestDTO request = new RoomRequestDTO(1L);

        when(roomRepository.findById(anyLong())).thenThrow(RuntimeException.class);

        assertThrows(RuntimeException.class, () -> {
            roomService.updateRoom(request, 1L);
        });
    }

    @Test
    @DisplayName("It should delete a room successfully")
    void deleteRoom() {
        when(roomRepository.existsById(anyLong())).thenReturn(true);

        ResponseEntity<?> response = roomService.deleteRoom(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    @DisplayName("It should fail on delete a room with no exist id")
    void deleteRoomError() {
        when(roomRepository.existsById(anyLong())).thenReturn(false);

        ResponseEntity<?> response = roomService.deleteRoom(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}