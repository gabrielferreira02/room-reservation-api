package com.gabrielferreira02.roomReservation.service;

import com.gabrielferreira02.roomReservation.dto.UserRequestDTO;
import com.gabrielferreira02.roomReservation.entity.Role;
import com.gabrielferreira02.roomReservation.entity.UserEntity;
import com.gabrielferreira02.roomReservation.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserEntity> findAll() {
        return userRepository.findAll();
    }

    @Override
    public ResponseEntity<UserEntity> createUser(UserRequestDTO userDTO) {
        UserEntity user = new UserEntity();
        user.setName(userDTO.getName());
        user.setPassword(userDTO.getPassword());
        user.setCpf(userDTO.getCpf());
        user.setRoles(new HashSet<>(Set.of(Role.USER)));

        return ResponseEntity.ok(userRepository.save(user));
    }

    @Override
    public ResponseEntity<UserEntity> updateUser(UserRequestDTO userDTO, Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Id does not exists"));

        user.setPassword(userDTO.getPassword());
        user.setName(userDTO.getName());
        user.setCpf(userDTO.getCpf());

        return ResponseEntity.ok(userRepository.save(user));
    }

    @Override
    public ResponseEntity<?> deleteUser(Long id) {
        if(!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
