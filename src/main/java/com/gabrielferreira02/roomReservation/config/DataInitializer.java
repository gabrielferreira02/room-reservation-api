package com.gabrielferreira02.roomReservation.config;

import com.gabrielferreira02.roomReservation.entity.*;
import com.gabrielferreira02.roomReservation.repository.ReserveRepository;
import com.gabrielferreira02.roomReservation.repository.RoomRepository;
import com.gabrielferreira02.roomReservation.repository.TypeRepository;
import com.gabrielferreira02.roomReservation.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Configuration
public class DataInitializer {

    @Bean
    @Transactional
    CommandLineRunner init(UserRepository userRepository,
                           ReserveRepository reserveRepository,
                           PasswordEncoder passwordEncoder,
                           TypeRepository typeRepository,
                           RoomRepository roomRepository)
    {
        return args -> {
            if(userRepository.count() == 0) {
                UserEntity user1 = userRepository.save(new UserEntity(
                        null,
                        "user",
                        "66399471702",
                        passwordEncoder.encode("12345678"),
                        Set.of(Role.USER)
                ));

                UserEntity user2 = userRepository.save(new UserEntity(
                        null,
                        "admin",
                        "53323332504",
                        passwordEncoder.encode("12345678"),
                        Set.of(Role.USER, Role.ADMIN)
                ));

                TypeEntity type1 = typeRepository.save(new TypeEntity(
                        null,
                        "Basic",
                        "Basic description",
                        100
                ));

                TypeEntity type2 = typeRepository.save(new TypeEntity(
                        null,
                        "Twin",
                        "Twin description",
                        150
                ));

                RoomEntity room1 = roomRepository.save(new RoomEntity(
                        null, type1, List.of()
                ));

                RoomEntity room2 = roomRepository.save(new RoomEntity(
                        null, type1, List.of()
                ));

                RoomEntity room3 = roomRepository.save(new RoomEntity(
                        null, type2, List.of()
                ));

                reserveRepository.save(new ReserveEntity(
                        null,
                        user1,
                        2,
                        2 * type1.getPrice(),
                        room1,
                        LocalDateTime.now(),
                        LocalDateTime.now().plusDays(2)
                ));
            }
        };
    }
}
