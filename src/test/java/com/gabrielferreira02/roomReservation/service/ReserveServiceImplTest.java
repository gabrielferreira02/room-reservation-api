package com.gabrielferreira02.roomReservation.service;

import com.gabrielferreira02.roomReservation.dto.ReserveRequestDTO;
import com.gabrielferreira02.roomReservation.entity.*;
import com.gabrielferreira02.roomReservation.repository.ReserveRepository;
import com.gabrielferreira02.roomReservation.repository.RoomRepository;
import com.gabrielferreira02.roomReservation.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReserveServiceImplTest {

    @Mock
    private RoomRepository roomRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ReserveRepository reserveRepository;
    @InjectMocks
    private ReserveServiceImpl reserveService;

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

    private final UserEntity user = new UserEntity(
            1L,
            "User",
            "17824829707",
            "12345678",
            new HashSet<>(Set.of(Role.USER))
    );

    private final ReserveEntity reserve = new ReserveEntity(
            1L,
            user,
            2,
            2 * type.getPrice(),
            room,
            LocalDateTime.now(),
            LocalDateTime.now().plusDays(2)
    );

    @Test
    @DisplayName("It should return all reserves")
    void findAll() {
        ReserveEntity reserve2 = new ReserveEntity(
                1L,
                new UserEntity(),
                5,
                5 * type.getPrice(),
                new RoomEntity(),
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(5)
        );
        when(reserveRepository.findAll()).thenReturn(List.of(reserve, reserve2));

        List<ReserveEntity> response = reserveService.findAll();

        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals(reserve.getDays(), response.getFirst().getDays());
        assertEquals(user, response.getFirst().getUser());
        assertEquals(room, response.getFirst().getRoom());
        assertEquals(reserve.getDays(), response.getFirst().getDays());
        assertEquals(reserve.getTotal(), response.getFirst().getTotal());
    }

    @Test
    @DisplayName("It should return a reserve by id")
    void findById() {
        when(reserveRepository.findById(anyLong())).thenReturn(Optional.of(reserve));

        ReserveEntity response = reserveService.findById(1L);

        assertNotNull(response);
        assertEquals(reserve.getUser(), response.getUser());
        assertEquals(reserve.getDays(), response.getDays());
        assertEquals(reserve.getRoom(), response.getRoom());
        assertEquals(reserve.getTotal(), response.getTotal());
    }

    @Test
    @DisplayName("It should return all reserves of an user")
    void findReserveByUserId() {
        when(reserveRepository.findReserveByUserId(anyLong())).thenReturn(List.of(reserve));

        List<ReserveEntity> response = reserveService.findReserveByUserId(1L);

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(reserve.getDays(), response.getFirst().getDays());
        assertEquals(user, response.getFirst().getUser());
        assertEquals(user.getUsername(), response.getFirst().getUser().getUsername());
        assertEquals(user.getCpf(), response.getFirst().getUser().getCpf());
        assertEquals(room, response.getFirst().getRoom());
        assertEquals(reserve.getDays(), response.getFirst().getDays());
        assertEquals(reserve.getTotal(), response.getFirst().getTotal());
    }

    @Test
    @DisplayName("It should create a reserve successfully")
    void createReserve() {
        ReserveRequestDTO request = new ReserveRequestDTO(
                1L,
                1L,
                2
        );

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(roomRepository.findById(anyLong())).thenReturn(Optional.of(room));
        when(reserveRepository.save(any(ReserveEntity.class))).thenReturn(reserve);

        ResponseEntity<?> response = reserveService.createReserve(request);

        ReserveEntity body = (ReserveEntity) response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response);
        assertNotNull(body);
        assertEquals(reserve.getDays(), body.getDays());
        assertEquals(reserve.getUser(), body.getUser());
        assertEquals(reserve.getTotal(), body.getTotal());
        assertEquals(reserve.getRoom(), body.getRoom());
    }

    @Test
    @DisplayName("It should fail on create a reserve due to error in userId")
    void createReserveErrorCase1() {
        ReserveRequestDTO request = new ReserveRequestDTO(
                2L,
                1L,
                2
        );

        when(userRepository.findById(anyLong())).thenThrow(RuntimeException.class);

        assertThrows(RuntimeException.class, () -> {
            reserveService.createReserve(request);
        });
    }

    @Test
    @DisplayName("It should fail on create a reserve due to error in roomId")
    void createReserveErrorCase2() {
        ReserveRequestDTO request = new ReserveRequestDTO(
                2L,
                1L,
                2
        );

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(roomRepository.findById(anyLong())).thenThrow(RuntimeException.class);

        assertThrows(RuntimeException.class, () -> {
            reserveService.createReserve(request);
        });
    }

    @Test
    @DisplayName("It should fail on create a reserve due to days is less than 1")
    void createReserveErrorCase3() {
        ReserveRequestDTO request = new ReserveRequestDTO(
                2L,
                1L,
                0
        );

        assertThrows(RuntimeException.class, () -> {
            reserveService.createReserve(request);
        });
    }

    @Test
    @DisplayName("It should fail on create a reserve due to days is less than 1")
    void updateReserveErrorCase1() {
        ReserveRequestDTO request = new ReserveRequestDTO(
                2L,
                1L,
                0
        );

        assertThrows(RuntimeException.class, () -> {
            reserveService.updateReserve(request, 1L);
        });
    }

    @Test
    @DisplayName("It should fail on create a reserve due to error in reserveId")
    void updateReserveErrorCase2() {
        ReserveRequestDTO request = new ReserveRequestDTO(
                2L,
                1L,
                2
        );

        when(reserveRepository.findById(anyLong())).thenThrow(RuntimeException.class);

        assertThrows(RuntimeException.class, () -> {
            reserveService.updateReserve(request, 3L);
        });
    }

    @Test
    @DisplayName("It should fail on create a reserve due to error in userId")
    void updateReserveErrorCase3() {
        ReserveRequestDTO request = new ReserveRequestDTO(
                2L,
                1L,
                2
        );

        when(reserveRepository.findById(anyLong())).thenReturn(Optional.of(reserve));
        when(userRepository.findById(anyLong())).thenThrow(RuntimeException.class);

        assertThrows(RuntimeException.class, () -> {
            reserveService.updateReserve(request, 1L);
        });
    }

    @Test
    @DisplayName("It should fail on create a reserve due to error in roomId")
    void updateReserveErrorCase4() {
        ReserveRequestDTO request = new ReserveRequestDTO(
                2L,
                1L,
                2
        );

        when(reserveRepository.findById(anyLong())).thenReturn(Optional.of(reserve));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(roomRepository.findById(anyLong())).thenThrow(RuntimeException.class);

        assertThrows(RuntimeException.class, () -> {
            reserveService.updateReserve(request, 1L);
        });
    }

    @Test
    @DisplayName("It should update a reserve successfully")
    void updateReserve() {
        ReserveRequestDTO request = new ReserveRequestDTO(
                1L,
                1L,
                5
        );

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(roomRepository.findById(anyLong())).thenReturn(Optional.of(room));

        reserve.setDays(request.getDays());
        reserve.setTotal(request.getDays() * type.getPrice());
        reserve.setEndDate(LocalDateTime.now().plusDays(request.getDays()));
        when(reserveRepository.save(any(ReserveEntity.class))).thenReturn(reserve);

        ResponseEntity<?> response = reserveService.createReserve(request);

        ReserveEntity body = (ReserveEntity) response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response);
        assertNotNull(body);
        assertEquals(reserve.getDays(), body.getDays());
        assertEquals(reserve.getUser(), body.getUser());
        assertEquals(reserve.getTotal(), body.getTotal());
        assertEquals(reserve.getRoom(), body.getRoom());
    }

    @Test
    @DisplayName("it should delete a reserve successfully")
    void deleteReserve() {
        when(reserveRepository.existsById(anyLong())).thenReturn(true);

        ResponseEntity<?> response = reserveService.deleteReserve(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    @DisplayName("it should fail on delete a reserve")
    void deleteReserveError() {
        when(reserveRepository.existsById(anyLong())).thenReturn(false);

        ResponseEntity<?> response = reserveService.deleteReserve(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}