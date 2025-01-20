package com.gabrielferreira02.roomReservation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabrielferreira02.roomReservation.dto.ReserveRequestDTO;
import com.gabrielferreira02.roomReservation.entity.*;
import com.gabrielferreira02.roomReservation.service.ReserveServiceImpl;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

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
class ReserveControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReserveServiceImpl reserveService;

    private final ReserveEntity reserve1 = new ReserveEntity(
            1L,
            new UserEntity(1L, "user", "17824829707", "12345678", Set.of(Role.USER)),
            2,
            2 * 120,
            new RoomEntity(1L, new TypeEntity(1L, "basic", "description", 120), List.of()),
            LocalDateTime.now(),
            LocalDateTime.now().plusDays(2)
    );

    private final ReserveEntity reserve2 = new ReserveEntity(
            2L,
            new UserEntity(2L, "user", "17824829707", "12345678", Set.of(Role.USER)),
            1,
            100,
            new RoomEntity(1L, new TypeEntity(1L, "basic", "description", 100), List.of()),
            LocalDateTime.now(),
            LocalDateTime.now().plusDays(1)
    );

    @Test
    @DisplayName("It should return a list of all reserves")
    void findAll() throws Exception {
        when(reserveService.findAll()).thenReturn(List.of(reserve1, reserve2));

        mockMvc.perform(get("/reserves"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("It should return a reserve by id")
    void findById() throws Exception {
        when(reserveService.findById(anyLong())).thenReturn(reserve1);

        mockMvc.perform(get("/reserves/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.username").value(reserve1.getUser().getUsername()))
                .andExpect(jsonPath("$.days").value(reserve1.getDays()))
                .andExpect(jsonPath("$.total").value(reserve1.getTotal()));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("It should return a list of all reserves by user id")
    void findReserveByUserId() throws Exception {
        when(reserveService.findReserveByUserId(anyLong())).thenReturn(List.of(reserve2));

        mockMvc.perform(get("/reserves/user/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("It should create a reserve successfully")
    void createReserve() throws Exception {
        ReserveRequestDTO request = new ReserveRequestDTO(1L, 1L, 2);
        String body = objectMapper.writeValueAsString(request);

        when(reserveService.createReserve(any(ReserveRequestDTO.class))).thenReturn(ResponseEntity.ok(reserve1));

        mockMvc.perform(post("/reserves")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.user.username").value(reserve1.getUser().getUsername()))
                .andExpect(jsonPath("$.days").value(request.getDays()))
                .andExpect(jsonPath("$.room.roomType.name").value(reserve1.getRoom().getRoomType().getName()));

    }

    @Test
    @DisplayName("It should fail on create a reserve due to invalid permission")
    void createReserveErrorCase1() throws Exception {
        ReserveRequestDTO request = new ReserveRequestDTO(1L, 1L, 2);
        String body = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/reserves")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                ).andExpect(status().isForbidden());

    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("It should fail on create a reserve due to invalid userId")
    void createReserveErrorCase2() throws Exception {
        ReserveRequestDTO request = new ReserveRequestDTO(null, 1L, 2);
        String body = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/reserves")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
        ).andExpect(status().isBadRequest());

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("It should fail on create a reserve due to invalid roomId")
    void createReserveErrorCase3() throws Exception {
        ReserveRequestDTO request = new ReserveRequestDTO(1L, null, 2);
        String body = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/reserves")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
        ).andExpect(status().isBadRequest());

    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("It should fail on create a reserve due to days field is less than 1")
    void createReserveErrorCase4() throws Exception {
        ReserveRequestDTO request = new ReserveRequestDTO(null, 1L, 0);
        String body = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/reserves")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
        ).andExpect(status().isBadRequest());

    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("It should update a reserve successfully")
    void updateReserve() throws Exception {
        ReserveRequestDTO request = new ReserveRequestDTO(1L, 1L, 2);
        String body = objectMapper.writeValueAsString(request);

        when(reserveService.updateReserve(any(ReserveRequestDTO.class), anyLong())).thenReturn(ResponseEntity.ok(reserve1));

        mockMvc.perform(put("/reserves/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.user.username").value(reserve1.getUser().getUsername()))
                .andExpect(jsonPath("$.days").value(request.getDays()))
                .andExpect(jsonPath("$.room.roomType.name").value(reserve1.getRoom().getRoomType().getName()));
    }

    @Test
    @DisplayName("It should fail on update a reserve due to invalid permission")
    void updateReserveErrorCase1() throws Exception {
        ReserveRequestDTO request = new ReserveRequestDTO(1L, 1L, 2);
        String body = objectMapper.writeValueAsString(request);

        mockMvc.perform(put("/reserves/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
        ).andExpect(status().isForbidden());

    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("It should fail on update a reserve due to invalid userId")
    void updateReserveErrorCase2() throws Exception {
        ReserveRequestDTO request = new ReserveRequestDTO(null, 1L, 2);
        String body = objectMapper.writeValueAsString(request);

        mockMvc.perform(put("/reserves/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
        ).andExpect(status().isBadRequest());

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("It should fail on update a reserve due to invalid roomId")
    void updateReserveErrorCase3() throws Exception {
        ReserveRequestDTO request = new ReserveRequestDTO(1L, null, 2);
        String body = objectMapper.writeValueAsString(request);

        mockMvc.perform(put("/reserves/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
        ).andExpect(status().isBadRequest());

    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("It should fail on update a reserve due to days field is less than 1")
    void updateReserveErrorCase4() throws Exception {
        ReserveRequestDTO request = new ReserveRequestDTO(null, 1L, 0);
        String body = objectMapper.writeValueAsString(request);

        mockMvc.perform(put("/reserves/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
        ).andExpect(status().isBadRequest());

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("It should delete a reserve successfully")
    void deleteReserve() throws Exception {
        when(reserveService.deleteReserve(anyLong())).thenReturn(ResponseEntity.noContent().build());

        mockMvc.perform(delete("/reserves/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("It should fail on delete a reserve due to an invalid id")
    void deleteReserveErrorCase1() throws Exception {
        when(reserveService.deleteReserve(anyLong())).thenReturn(ResponseEntity.notFound().build());

        mockMvc.perform(delete("/reserves/3"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("It should fail on delete a reserve due to invalid permission")
    void deleteReserveErrorCase2() throws Exception {
        mockMvc.perform(delete("/reserves/1"))
                .andExpect(status().isForbidden());
    }
}