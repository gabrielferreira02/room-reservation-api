package com.gabrielferreira02.roomReservation.service;

import com.gabrielferreira02.roomReservation.dto.UserRequestDTO;
import com.gabrielferreira02.roomReservation.entity.Role;
import com.gabrielferreira02.roomReservation.entity.UserEntity;
import com.gabrielferreira02.roomReservation.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserServiceImpl userService;

    private final UserEntity user = new UserEntity(
            1L,
            "User",
            "17824829707",
            "12345678",
            new HashSet<>(Set.of(Role.USER))
    );

    @Test
    @DisplayName("It should return a list of users")
    void findAll() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<UserEntity> users = userService.findAll();

        assertEquals(1, users.size());
        assertEquals("User", users.getFirst().getUsername());
        assertEquals("17824829707", users.getFirst().getCpf());
    }

    @Test
    @DisplayName("It should create a user with success")
    void createUser() {
        UserRequestDTO request = new UserRequestDTO(
                "User",
                "12345678",
                "17824829707"
        );
        when(userRepository.save(any(UserEntity.class))).thenReturn(user);

        ResponseEntity<?> response = userService.createUser(request);

        UserEntity body = (UserEntity) response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response);
        assertNotNull(body);
        assertEquals(request.getName(), body.getUsername());
        assertEquals(request.getCpf(), body.getCpf());
    }

    @Test
    @DisplayName("It should fail on create a user due to invalid cpf")
    void createUserErrorCase1() {
        UserRequestDTO request = new UserRequestDTO(
                "User",
                "12345678",
                "17824829701"
        );
        when(userRepository.save(any(UserEntity.class))).thenThrow(RuntimeException.class);

        assertThrows(RuntimeException.class, () -> {
            userService.createUser(request);
        });
    }

    @Test
    @DisplayName("It should fail on create a user due to password length is less than 8")
    void createUserErrorCase2() {
        UserRequestDTO request = new UserRequestDTO(
                "User",
                "1234567",
                "17824829707"
        );
        when(userRepository.save(any(UserEntity.class))).thenThrow(RuntimeException.class);

        assertThrows(RuntimeException.class, () -> {
            userService.createUser(request);
        });
    }

    @Test
    @DisplayName("It should fail on create a user due to name field is blank")
    void createUserErrorCase3() {
        UserRequestDTO request = new UserRequestDTO(
                "",
                "12345678",
                "17824829707"
        );
        when(userRepository.save(any(UserEntity.class))).thenThrow(RuntimeException.class);

        assertThrows(RuntimeException.class, () -> {
            userService.createUser(request);
        });
    }

    @Test
    @DisplayName("It should update a user successfully")
    void updateUser() {
        UserRequestDTO request = new UserRequestDTO(
                "User Updated",
                "12345678",
                "17824829707"
        );

        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));

        UserEntity updatedUser = user;
        updatedUser.setPassword(request.getPassword());
        updatedUser.setUsername(request.getName());
        updatedUser.setCpf(request.getCpf());

        when(userRepository.save(any(UserEntity.class))).thenReturn(updatedUser);

        ResponseEntity<?> response = userService.updateUser(request, 1L);

        UserEntity body = (UserEntity) response.getBody();

        assertNotNull(response);
        assertNotNull(body);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(request.getName(), body.getUsername());
        assertEquals(request.getCpf(), body.getCpf());
    }

    @Test
    @DisplayName("It should fail on update a user due to name field is blank")
    void updateUserErrorCase1() {
        UserRequestDTO request = new UserRequestDTO(
                "",
                "1234567",
                "17824829707"
        );

        assertThrows(RuntimeException.class, () -> {
            userService.updateUser(request, 1L);
        });
    }

    @Test
    @DisplayName("It should fail on update a user due to password length is less than 8")
    void updateUserErrorCase2() {
        UserRequestDTO request = new UserRequestDTO(
                "User",
                "1234567",
                "17824829707"
        );

        assertThrows(RuntimeException.class, () -> {
            userService.updateUser(request, 1L);
        });
    }

    @Test
    @DisplayName("It should fail on update a user due to invalid cpf")
    void updateUserErrorCase3() {
        UserRequestDTO request = new UserRequestDTO(
                "User",
                "1234567",
                "17824829701"
        );

        assertThrows(RuntimeException.class, () -> {
            userService.updateUser(request, 1L);
        });
    }

    @Test
    @DisplayName("It should delete a user successfully")
    void deleteUser() {
        when(userRepository.existsById(any(Long.class))).thenReturn(true);

        ResponseEntity<?> response = userService.deleteUser(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    @DisplayName("It should fail on delete a user")
    void deleteUserError() {
        when(userRepository.existsById(any(Long.class))).thenReturn(false);

        ResponseEntity<?> response = userService.deleteUser(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}