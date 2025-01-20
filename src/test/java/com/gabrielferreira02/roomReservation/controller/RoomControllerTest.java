package com.gabrielferreira02.roomReservation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabrielferreira02.roomReservation.dto.RoomRequestDTO;
import com.gabrielferreira02.roomReservation.entity.ReserveEntity;
import com.gabrielferreira02.roomReservation.entity.RoomEntity;
import com.gabrielferreira02.roomReservation.entity.TypeEntity;
import com.gabrielferreira02.roomReservation.service.RoomServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class RoomControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RoomServiceImpl roomService;

    private final RoomEntity room = new RoomEntity(
            1L,
            new TypeEntity(1L, "Basic", "description", 120),
            List.of()
    );

    @Test
    @DisplayName("It should return a list with all rooms")
    void findAll() throws Exception {
         RoomEntity room2 = new RoomEntity(
                1L,
                new TypeEntity(1L, "Basic", "description", 120),
                List.of(new ReserveEntity())
        );

         when(roomService.findAll()).thenReturn(List.of(this.room, room2));

         mockMvc.perform(get("/rooms")
                 .contentType(MediaType.APPLICATION_JSON))
                 .andExpect(status().isOk())
                 .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @DisplayName("It should return a list with all free rooms")
    void findAllFreeRooms() throws Exception {
        when(roomService.findAll()).thenReturn(List.of(this.room));

        mockMvc.perform(get("/rooms")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @DisplayName("It should create a room successfully")
    @WithMockUser(roles = "ADMIN")
    void createRoom() throws Exception {
        RoomRequestDTO request = new RoomRequestDTO(1L);
        String body = objectMapper.writeValueAsString(request);

        when(roomService.createRoom(any(RoomRequestDTO.class))).thenReturn(ResponseEntity.ok(this.room));

        mockMvc.perform(post("/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.roomType.name").value(room.getRoomType().getName()))
                .andExpect(jsonPath("$.roomType.description").value(room.getRoomType().getDescription()))
                .andExpect(jsonPath("$.roomType.price").value(room.getRoomType().getPrice()));
    }

    @Test
    @DisplayName("It should fail on create a room due to invalid role")
    @WithMockUser(roles = "USER")
    void createRoomErrorCase1() throws Exception {
        RoomRequestDTO request = new RoomRequestDTO(1L);
        String body = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                ).andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("It should fail on create a room due to invalid argument")
    @WithMockUser(roles = "ADMIN")
    void createRoomErrorCase2() throws Exception {
        RoomRequestDTO request = new RoomRequestDTO(null);
        String body = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
        ).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("It should update a room successfully")
    void updateRoom() throws Exception {
        RoomRequestDTO request = new RoomRequestDTO(1L);
        String body = objectMapper.writeValueAsString(request);

        when(roomService.updateRoom(any(RoomRequestDTO.class), anyLong())).thenReturn(ResponseEntity.ok(this.room));

        mockMvc.perform(put("/rooms/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.roomType.name").value(room.getRoomType().getName()))
                .andExpect(jsonPath("$.roomType.description").value(room.getRoomType().getDescription()))
                .andExpect(jsonPath("$.roomType.price").value(room.getRoomType().getPrice()));
    }

    @Test
    @DisplayName("It should fail on update a room due to invalid role")
    @WithMockUser(roles = "USER")
    void updateRoomErrorCase1() throws Exception {
        RoomRequestDTO request = new RoomRequestDTO(1L);
        String body = objectMapper.writeValueAsString(request);

        mockMvc.perform(put("/rooms/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
        ).andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("It should fail on update a room due to invalid argument")
    @WithMockUser(roles = "ADMIN")
    void updateRoomErrorCase2() throws Exception {
        RoomRequestDTO request = new RoomRequestDTO(null);
        String body = objectMapper.writeValueAsString(request);

        mockMvc.perform(put("/rooms/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
        ).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("It should delete a room successfully")
    @WithMockUser(roles = "ADMIN")
    void deleteRoom() throws Exception {
        when(roomService.deleteRoom(anyLong())).thenReturn(ResponseEntity.noContent().build());

        mockMvc.perform(delete("/rooms/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("It should fail on delete a room due to invalid role")
    @WithMockUser(roles = "USER")
    void deleteRoomErrorCase1() throws Exception {
        when(roomService.deleteRoom(anyLong())).thenReturn(ResponseEntity.noContent().build());

        mockMvc.perform(delete("/rooms/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("It should fail on delete a room due to invalid id")
    @WithMockUser(roles = "ADMIN")
    void deleteRoomErrorCase2() throws Exception {
        when(roomService.deleteRoom(anyLong())).thenReturn(ResponseEntity.notFound().build());

        mockMvc.perform(delete("/rooms/2"))
                .andExpect(status().isNotFound());
    }
}