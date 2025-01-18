package com.gabrielferreira02.roomReservation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabrielferreira02.roomReservation.dto.UserRequestDTO;
import com.gabrielferreira02.roomReservation.entity.Role;
import com.gabrielferreira02.roomReservation.entity.UserEntity;
import com.gabrielferreira02.roomReservation.service.UserServiceImpl;
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
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserServiceImpl userService;

    @Test
    @DisplayName("It should return a list of users")
    void findAll() throws Exception {
        UserEntity user = new UserEntity(1L, "user", "17824829707", "12345678", Set.of(Role.USER));

        when(userService.findAll()).thenReturn(List.of(user));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @DisplayName("It should create an user successfully")
    void createUser() throws Exception {
        UserRequestDTO request = new UserRequestDTO("user", "12345678","17824829707");
        String body = objectMapper.writeValueAsString(request);
        UserEntity user = new UserEntity(
                1L,
                request.getName(),
                request.getCpf(),
                request.getPassword(),
                Set.of(Role.USER)
        );

        when(userService.createUser(any(UserRequestDTO.class))).thenReturn(ResponseEntity.ok(user));

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(request.getName()))
                .andExpect(jsonPath("$.cpf").value(request.getCpf()));
    }

    @Test
    @DisplayName("It should fail on create an user due to bad arguments")
    void createUserErrorCase1() throws Exception {
        UserRequestDTO request = new UserRequestDTO("", "12345678","17824829707");
        String body = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                ).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("It should fail on create an user due to invalid cpf")
    void createUserErrorCase2() throws Exception {
        UserRequestDTO request = new UserRequestDTO("user", "12345678","17824829701");
        String body = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
        ).andExpect(status().isBadRequest());
    }


    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("It should update an user successfully")
    void updateUser() throws Exception {
        UserRequestDTO request = new UserRequestDTO("user", "12345678","17824829707");
        String body = objectMapper.writeValueAsString(request);
        UserEntity user = new UserEntity(
                1L,
                request.getName(),
                request.getCpf(),
                request.getPassword(),
                Set.of(Role.USER)
        );

        when(userService.updateUser(any(UserRequestDTO.class), anyLong())).thenReturn(ResponseEntity.ok(user));

        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(request.getName()))
                .andExpect(jsonPath("$.cpf").value(request.getCpf()));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("It should fail on update an user due to bad arguments")
    void updateUserErrorCase1() throws Exception {
        UserRequestDTO request = new UserRequestDTO("", "12345678","17824829707");
        String body = objectMapper.writeValueAsString(request);

        mockMvc.perform(put("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
        ).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("It should fail on update an user due to invalid cpf")
    void updateUserErrorCase2() throws Exception {
        UserRequestDTO request = new UserRequestDTO("user", "12345678","17824829701");
        String body = objectMapper.writeValueAsString(request);

        mockMvc.perform(put("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
        ).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("It should fail on update an user due to access denied")
    void updateUserErrorCase4() throws Exception {
        UserRequestDTO request = new UserRequestDTO("user", "12345678","17824829707");
        String body = objectMapper.writeValueAsString(request);

        mockMvc.perform(put("/users/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
        ).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("It should delete a user successfully")
    void deleteUser() throws Exception {
        when(userService.deleteUser(anyLong())).thenReturn(ResponseEntity.noContent().build());

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("It should fail on delete a user")
    void deleteUserError() throws Exception {
        when(userService.deleteUser(anyLong())).thenReturn(ResponseEntity.notFound().build());

        mockMvc.perform(delete("/users/2"))
                .andExpect(status().isNotFound());
    }
}