package com.gabrielferreira02.roomReservation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabrielferreira02.roomReservation.dto.TypeRequestDTO;
import com.gabrielferreira02.roomReservation.entity.TypeEntity;
import com.gabrielferreira02.roomReservation.service.TypeServiceImpl;
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
@ActiveProfiles("test")
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class TypeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TypeServiceImpl typeService;

    @Test
    @DisplayName("It should return a list of room types")
    void findAll() throws Exception {
        TypeEntity type = new TypeEntity(1L, "Basic", "Basic description", 120);

        when(typeService.findAll()).thenReturn(List.of(type));

        mockMvc.perform(get("/room-types")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("It should create a type successfully")
    void createType() throws Exception {
        TypeRequestDTO type = new TypeRequestDTO("Basic", "Description", 120);
        String body = objectMapper.writeValueAsString(type);
        TypeEntity createdType = new TypeEntity(1L, type.getName(), type.getDescription(), type.getPrice());

        when(typeService.createType(any(TypeRequestDTO.class))).thenReturn(ResponseEntity.ok(createdType));

        mockMvc.perform(post("/room-types")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(createdType.getName()))
                .andExpect(jsonPath("$.description").value(createdType.getDescription()))
                .andExpect(jsonPath("$.price").value(createdType.getPrice()));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("It should fail on create a type due to role user")
    void createTypeErrorCase1() throws Exception {
        TypeRequestDTO type = new TypeRequestDTO("Basic", "Description", 120);
        String body = objectMapper.writeValueAsString(type);

        mockMvc.perform(post("/room-types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                ).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("It should fail on create a type due to no name")
    void createTypeErrorCase2() throws Exception {
        TypeRequestDTO type = new TypeRequestDTO("", "Description", 120);
        String body = objectMapper.writeValueAsString(type);

        mockMvc.perform(post("/room-types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                ).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("It should fail on create a type due to no description")
    void createTypeErrorCase3() throws Exception {
        TypeRequestDTO type = new TypeRequestDTO("Basic", "", 120);
        String body = objectMapper.writeValueAsString(type);

        mockMvc.perform(post("/room-types")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
        ).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("It should fail on create a type due to invalid price")
    void createTypeErrorCase4() throws Exception {
        TypeRequestDTO type = new TypeRequestDTO("Basic", "Description", 10);
        String body = objectMapper.writeValueAsString(type);

        mockMvc.perform(post("/room-types")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
        ).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("It should update a type successfully")
    void updateType() throws Exception {
        TypeRequestDTO type = new TypeRequestDTO("Basic", "Description", 120);
        String body = objectMapper.writeValueAsString(type);
        TypeEntity createdType = new TypeEntity(1L, type.getName(), type.getDescription(), type.getPrice());

        when(typeService.updateType(any(TypeRequestDTO.class), anyLong())).thenReturn(ResponseEntity.ok(createdType));

        mockMvc.perform(put("/room-types/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(createdType.getName()))
                .andExpect(jsonPath("$.description").value(createdType.getDescription()))
                .andExpect(jsonPath("$.price").value(createdType.getPrice()));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("It should fail on update a type due to role user")
    void updateTypeErrorCase1() throws Exception {
        TypeRequestDTO type = new TypeRequestDTO("Basic", "Description", 120);
        String body = objectMapper.writeValueAsString(type);

        mockMvc.perform(put("/room-types/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
        ).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("It should fail on update a type due to no name")
    void updateTypeErrorCase2() throws Exception {
        TypeRequestDTO type = new TypeRequestDTO("", "Description", 120);
        String body = objectMapper.writeValueAsString(type);

        mockMvc.perform(put("/room-types/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
        ).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("It should fail on update a type due to no description")
    void updateTypeErrorCase3() throws Exception {
        TypeRequestDTO type = new TypeRequestDTO("Basic", "", 120);
        String body = objectMapper.writeValueAsString(type);

        mockMvc.perform(put("/room-types/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
        ).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("It should fail on update a type due to invalid price")
    void updateTypeErrorCase4() throws Exception {
        TypeRequestDTO type = new TypeRequestDTO("basic", "Description", 10);
        String body = objectMapper.writeValueAsString(type);

        mockMvc.perform(put("/room-types/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
        ).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("It should fail on update a type due to invalid id")
    void updateTypeErrorCase5() throws Exception {
        TypeRequestDTO type = new TypeRequestDTO("basic", "Description", 10);
        String body = objectMapper.writeValueAsString(type);

        when(typeService.updateType(any(TypeRequestDTO.class), anyLong())).thenThrow(RuntimeException.class);

        mockMvc.perform(put("/room-types/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
        ).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("It should fail on delete a user with invalid id")
    void deleteType() throws Exception {
        when(typeService.deleteType(anyLong())).thenReturn(ResponseEntity.noContent().build());

        mockMvc.perform(delete("/room-types/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("It should fail on delete due to an invalid id")
    void deleteTypeErrorCase1() throws Exception {
        when(typeService.deleteType(anyLong())).thenReturn(ResponseEntity.notFound().build());

        mockMvc.perform(delete("/room-types/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("It should deny an account with only user role to delete a room type")
    void deleteTypeErrorCase2() throws Exception {
        mockMvc.perform(delete("/room-types/1"))
                .andExpect(status().isForbidden());
    }
}