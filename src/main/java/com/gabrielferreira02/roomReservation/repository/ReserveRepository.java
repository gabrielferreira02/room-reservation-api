package com.gabrielferreira02.roomReservation.repository;

import com.gabrielferreira02.roomReservation.entity.ReserveEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReserveRepository extends JpaRepository<ReserveEntity, Long> {
}
