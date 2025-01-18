package com.gabrielferreira02.roomReservation.service;

import com.gabrielferreira02.roomReservation.dto.ReserveRequestDTO;
import com.gabrielferreira02.roomReservation.entity.ReserveEntity;
import com.gabrielferreira02.roomReservation.entity.RoomEntity;
import com.gabrielferreira02.roomReservation.entity.UserEntity;
import com.gabrielferreira02.roomReservation.repository.ReserveRepository;
import com.gabrielferreira02.roomReservation.repository.RoomRepository;
import com.gabrielferreira02.roomReservation.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReserveServiceImpl implements ReserveService {

    @Autowired
    private ReserveRepository reserveRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoomRepository roomRepository;

    @Override
    public List<ReserveEntity> findAll() {
        return reserveRepository.findAll();
    }

    @Override
    public ReserveEntity findById(Long id) {
        return reserveRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Id does not exists"));
    }

    @Override
    public List<ReserveEntity> findReserveByUserId(Long id) {
        return reserveRepository.findReserveByUserId(id);
    }

    @Override
    public ResponseEntity<ReserveEntity> createReserve(ReserveRequestDTO reserveDTO) {
        UserEntity user = userRepository.findById(reserveDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User id does not exists"));

        List<ReserveEntity> r = reserveRepository.findByRoomId(reserveDTO.getRoomId());

        if(!r.isEmpty()) {
            throw new RuntimeException("The room is already reserved");
        }

        RoomEntity room = roomRepository.findById(reserveDTO.getRoomId())
                .orElseThrow(() -> new RuntimeException("Room id does not exists"));

        ReserveEntity reserve = new ReserveEntity();
        reserve.setUser(user);
        reserve.setRoom(room);
        reserve.setTotal(reserveDTO.getDays() * room.getRoomType().getPrice());
        reserve.setDays(reserveDTO.getDays());
        reserve.setStartDate(LocalDateTime.now());
        reserve.setEndDate(LocalDateTime.now().plusDays(reserve.getDays()));

        return ResponseEntity.ok(reserveRepository.save(reserve));
    }

    @Override
    public ResponseEntity<ReserveEntity> updateReserve(ReserveRequestDTO reserveDTO, Long id) {
        ReserveEntity reserve = reserveRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserve id does not exists"));

        UserEntity user = userRepository.findById(reserveDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User id does not exists"));

        RoomEntity room = roomRepository.findById(reserveDTO.getRoomId())
                .orElseThrow(() -> new RuntimeException("Room id does not exists"));

        reserve.setUser(user);
        reserve.setRoom(room);
        reserve.setDays(reserve.getDays());
        reserve.setTotal(reserveDTO.getDays() * room.getRoomType().getPrice());
        reserve.setStartDate(LocalDateTime.now());
        reserve.setEndDate(LocalDateTime.now().plusDays(reserve.getDays()));

        return ResponseEntity.ok(reserveRepository.save(reserve));
    }

    @Override
    public ResponseEntity<?> deleteReserve(Long id) {
        if(!reserveRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        reserveRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
