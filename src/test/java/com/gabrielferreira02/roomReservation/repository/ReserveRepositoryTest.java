package com.gabrielferreira02.roomReservation.repository;

import com.gabrielferreira02.roomReservation.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class ReserveRepositoryTest {

    @Autowired
    private TypeRepository typeRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private ReserveRepository reserveRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        TypeEntity type1 = typeRepository.save(new TypeEntity(null,"basic", "dsncdsn", 150));
        TypeEntity type2 = typeRepository.save(new TypeEntity(null,"twin", "smndlksdk", 200));
        UserEntity user = userRepository.save(new UserEntity(null, "snSSAMDA", "17824829707", "12345678", Set.of(Role.USER)));

        RoomEntity room1 = new RoomEntity(
                null,
                type1,
                List.of()
        );

        RoomEntity room2 = new RoomEntity(
                null,
                type2,
                List.of()
        );

        ReserveEntity reserve = new ReserveEntity(
                null,
                user,
                2,
                room1.getRoomType().getPrice() * 2,
                room1,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(2)
        );

        ReserveEntity reserve2 = new ReserveEntity(
                null,
                user,
                3,
                room1.getRoomType().getPrice() * 3,
                room2,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(3)
        );

        roomRepository.save(room1);
        roomRepository.save(room2);
        reserveRepository.save(reserve);
        reserveRepository.save(reserve2);
    }

    @Test
    void findReserveByUserId() {
        List<ReserveEntity> reservesByUserId = reserveRepository.findReserveByUserId(userRepository.findAll().getFirst().getId());

        assertEquals(2, reservesByUserId.size());
        assertEquals(2, reservesByUserId.getFirst().getDays());
        assertEquals(userRepository.findAll().getFirst().getUsername(), reservesByUserId.getFirst().getUser().getUsername());
    }

    @Test
    void findByRoomId() {
        List<ReserveEntity> reservesByRoomId = reserveRepository.findByRoomId(roomRepository.findAll().getFirst().getId());

        assertEquals(1, reservesByRoomId.size());
        assertEquals(roomRepository.findAll().getFirst().getRoomType().getName(), reservesByRoomId.getFirst().getRoom().getRoomType().getName());
    }
}