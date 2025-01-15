package com.gabrielferreira02.roomReservation.repository;

import com.gabrielferreira02.roomReservation.entity.TypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeRepository extends JpaRepository<TypeEntity, Long> {
}
