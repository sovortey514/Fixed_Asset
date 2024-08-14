package com.twd.SpringSecurityJWT.repository;

import com.twd.SpringSecurityJWT.entity.Room;

import java.util.List;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    @Query("SELECT fa FROM Room fa JOIN FETCH fa.department JOIN FETCH fa.building")
    List<Room> findAllWithDepartmentWithBuilding();

    Optional<Room> findByName(String name);
}
