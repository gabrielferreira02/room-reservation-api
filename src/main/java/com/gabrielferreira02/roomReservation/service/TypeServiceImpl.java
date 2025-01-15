package com.gabrielferreira02.roomReservation.service;

import com.gabrielferreira02.roomReservation.dto.TypeRequestDTO;
import com.gabrielferreira02.roomReservation.entity.TypeEntity;
import com.gabrielferreira02.roomReservation.repository.TypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TypeServiceImpl implements TypeService {

    @Autowired
    private TypeRepository typeRepository;

    @Override
    public List<TypeEntity> findAll() {
        return typeRepository.findAll();
    }

    @Override
    public ResponseEntity<TypeEntity> createType(TypeRequestDTO typeDTO) {
        TypeEntity type = new TypeEntity();
        type.setName(typeDTO.getName());
        type.setPrice(typeDTO.getPrice());
        type.setDescription(typeDTO.getDescription());

        return ResponseEntity.ok(typeRepository.save(type));
    }

    @Override
    public ResponseEntity<TypeEntity> updateType(TypeRequestDTO typeDTO, Long id) {
        TypeEntity type = typeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Id not found"));

        type.setPrice(typeDTO.getPrice());
        type.setName(typeDTO.getName());
        type.setDescription(typeDTO.getDescription());

        return ResponseEntity.ok(typeRepository.save(type));
    }

    @Override
    public ResponseEntity<?> deleteType(Long id) {
        if(!typeRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        typeRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
