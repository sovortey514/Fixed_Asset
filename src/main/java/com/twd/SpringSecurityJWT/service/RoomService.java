package com.twd.SpringSecurityJWT.service;

import com.twd.SpringSecurityJWT.entity.Room;

import java.util.List;
import java.util.Optional;

public interface RoomService {
    Room createRoom(Room room);
    void deleteRoom(Long id);
    List<Room> getAllRooms();
    Optional<Room> getRoomById(Long id);
    Room updateRoom(Long id, Room room);
}
