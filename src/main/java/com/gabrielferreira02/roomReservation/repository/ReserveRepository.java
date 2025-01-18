package com.gabrielferreira02.roomReservation.repository;

import com.gabrielferreira02.roomReservation.entity.ReserveEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReserveRepository extends JpaRepository<ReserveEntity, Long> {
    @Query("SELECT r FROM ReserveEntity r WHERE r.user.id = :userId")
    List<ReserveEntity> findReserveByUserId(Long userId);
    @Query("SELECT r FROM ReserveEntity r WHERE r.room.id = :roomId")
    List<ReserveEntity> findByRoomId(Long roomId);
}
