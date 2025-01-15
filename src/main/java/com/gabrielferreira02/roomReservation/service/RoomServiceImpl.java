package com.gabrielferreira02.roomReservation.service;

import com.gabrielferreira02.roomReservation.dto.RoomRequestDTO;
import com.gabrielferreira02.roomReservation.entity.RoomEntity;
import com.gabrielferreira02.roomReservation.entity.TypeEntity;
import com.gabrielferreira02.roomReservation.repository.RoomRepository;
import com.gabrielferreira02.roomReservation.repository.TypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomServiceImpl implements RoomService{

    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private TypeRepository typeRepository;

    @Override
    public List<RoomEntity> findAll() {
        return roomRepository.findAll();
    }

    @Override
    public List<RoomEntity> findAllFreeRooms() {
        return roomRepository.findAllFreeRooms();
    }

    @Override
    public ResponseEntity<RoomEntity> createRoom(RoomRequestDTO roomDTO) {
        RoomEntity room = new RoomEntity();

        TypeEntity type = typeRepository.findById(roomDTO.getTypeId())
                        .orElseThrow(() -> new RuntimeException("Type id not found"));

        room.setRoomType(type);

        return ResponseEntity.ok(roomRepository.save(room));
    }

    @Override
    public ResponseEntity<RoomEntity> updateRoom(RoomRequestDTO roomDTO, Long id) {
        RoomEntity room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room id not found"));

        TypeEntity type = typeRepository.findById(room.getId())
                .orElseThrow(() -> new RuntimeException("Type id not found"));

        room.setRoomType(type);

        return ResponseEntity.ok(roomRepository.save(room));
    }

    @Override
    public ResponseEntity<?> deleteRoom(Long id) {
        if(!roomRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        roomRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
