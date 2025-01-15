package com.gabrielferreira02.roomReservation.repository;

import com.gabrielferreira02.roomReservation.entity.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<RoomEntity, Long> {
    @Query("SELECT q FROM RoomEntity q " +
            " LEFT JOIN q.reserves r " +
            " WHERE r IS NULL")
    List<RoomEntity> findAllFreeRooms();
}
