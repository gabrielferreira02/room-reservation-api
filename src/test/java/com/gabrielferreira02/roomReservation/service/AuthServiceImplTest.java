package com.gabrielferreira02.roomReservation.service;

import com.gabrielferreira02.roomReservation.dto.JwtResponseDTO;
import com.gabrielferreira02.roomReservation.dto.LoginDTO;
import com.gabrielferreira02.roomReservation.utils.JwtUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtUtils jwtUtils;
    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    @DisplayName("It should login a user successfully")
    void login() {
        String username = "User";
        String password = "12345678";
        String token = "mockedToken";

        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        )).thenReturn(authentication);

        when(authentication.getName()).thenReturn(username);
        when(jwtUtils.generateToken(anyString())).thenReturn(token);

        ResponseEntity<?> response = authService.login(new LoginDTO(username, password));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response);
        assertEquals(token, ((JwtResponseDTO) response.getBody()).token());
    }

    @Test
    @DisplayName("It should deny the login credentials")
    void loginError() {
        String username = "user1";
        String password = "12345678";

        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        )).thenThrow(BadCredentialsException.class);

        assertThrows(BadCredentialsException.class, () -> {
            authService.login(new LoginDTO(username, password));
        });
    }
}