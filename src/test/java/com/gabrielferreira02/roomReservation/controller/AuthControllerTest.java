package com.gabrielferreira02.roomReservation.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabrielferreira02.roomReservation.dto.JwtResponseDTO;
import com.gabrielferreira02.roomReservation.dto.LoginDTO;
import com.gabrielferreira02.roomReservation.service.AuthServiceImpl;
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
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthServiceImpl authService;

    @Test
    @DisplayName("It should login an user successfully")
    void login() throws Exception {
        LoginDTO request = new LoginDTO("user", "12345678");

        String body = objectMapper.writeValueAsString(request);

        ResponseEntity<JwtResponseDTO> response = ResponseEntity.ok(new JwtResponseDTO("token"));

        when(authService.login(any(LoginDTO.class))).thenReturn(response);

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token"));
    }

    @Test
    @DisplayName("It should fail on login an user")
    void loginError() throws Exception {
        LoginDTO request = new LoginDTO("", "12345678");

        String body = objectMapper.writeValueAsString(request);

        when(authService.login(any(LoginDTO.class))).thenThrow(BadCredentialsException.class);

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                )
                .andExpect(status().isForbidden());
    }
}