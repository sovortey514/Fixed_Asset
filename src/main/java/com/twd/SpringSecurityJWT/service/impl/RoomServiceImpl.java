package com.twd.SpringSecurityJWT.service.impl;

import com.twd.SpringSecurityJWT.entity.Building;
import com.twd.SpringSecurityJWT.entity.Department;
import com.twd.SpringSecurityJWT.entity.Room;
import com.twd.SpringSecurityJWT.repository.BuildingRepository;
import com.twd.SpringSecurityJWT.repository.DepartmentRepository;
import com.twd.SpringSecurityJWT.repository.RoomRepository;
import com.twd.SpringSecurityJWT.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private BuildingRepository buildingRepository;



   @Override
    public Room createRoom(Room room) {
        Department department = departmentRepository.findById(room.getDepartment().getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid department ID"));

        Building building = buildingRepository.findById(room.getBuilding().getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid building ID"));

        room.setDepartment(department);
        room.setBuilding(building);
        return roomRepository.save(room);
    }

    @Override
    public void deleteRoom(Long id) {
        roomRepository.deleteById(id);
    }

    @Override
    public List<Room> getAllRooms() {
        return roomRepository.findAllWithDepartmentWithBuilding();
    }

    @Override
    public Optional<Room> getRoomById(Long id) {
        return roomRepository.findById(id);
    }

    @Override
    public Room updateRoom(Long id, Room room) {
        Optional<Room> existingRoom = roomRepository.findById(id);
        if (existingRoom.isPresent()) {
            Room updatedRoom = existingRoom.get();
            updatedRoom.setName(room.getName());
            updatedRoom.setDescription(room.getDescription());
            updatedRoom.setCapacity(room.getCapacity());
            updatedRoom.setDepartment(room.getDepartment());
            updatedRoom.setUpdatedAt(LocalDateTime.now());
            validateRoom(updatedRoom);
            return roomRepository.save(updatedRoom);
        } else {
            throw new IllegalArgumentException("Room not found");
        }
    }

    private void validateRoom(Room room) {
        if (room.getName() == null || room.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Room name must not be empty");
        }
    }
}
