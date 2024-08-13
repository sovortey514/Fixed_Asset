package com.twd.SpringSecurityJWT.controller;

import com.twd.SpringSecurityJWT.entity.Room;
import com.twd.SpringSecurityJWT.service.RoomService;
import com.twd.SpringSecurityJWT.dto.ReqRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @PostMapping("/createRoom")
    public ResponseEntity<ReqRes> createRoom(@RequestBody Room room) {
        ReqRes resp = new ReqRes();
        try {
            Room createdRoom = roomService.createRoom(room);
            
            resp.setRoom(createdRoom);
            resp.setMessage("Room Created Successfully");
            resp.setStatusCode(200);
        } catch (IllegalArgumentException e) {
            resp.setStatusCode(400);
            resp.setError(e.getMessage());
        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return ResponseEntity.ok(resp);
    }

    @DeleteMapping("/deleteroom/{id}")
    public ResponseEntity<ReqRes> deleteRoom(@PathVariable Long id) {
        ReqRes resp = new ReqRes();
        try {
            roomService.deleteRoom(id);
            resp.setMessage("Room Deleted Successfully");
            resp.setStatusCode(200);
        } catch (IllegalArgumentException e) {
            resp.setStatusCode(404);
            resp.setError(e.getMessage());
        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/getAllRooms")
    public ResponseEntity<ReqRes> getAllRooms() {
    ReqRes resp = new ReqRes();
    try {
        List<Room> rooms = roomService.getAllRooms();
        resp.setRooms(rooms);
        resp.setMessage("Rooms Retrieved Successfully");
        resp.setStatusCode(200);
    } catch (Exception e) {
        resp.setStatusCode(500);
        resp.setError(e.getMessage());
    }
    return ResponseEntity.ok(resp);
    }

    @GetMapping("getroomById/{id}")
    public ResponseEntity<ReqRes> getRoomById(@PathVariable Long id) {
        ReqRes resp = new ReqRes();
        try {
            Optional<Room> room = roomService.getRoomById(id);
            if (room.isPresent()) {
                resp.setRoom(room.get());
                resp.setMessage("Room Retrieved Successfully");
                resp.setStatusCode(200);
            } else {
                resp.setMessage("Room Not Found");
                resp.setStatusCode(404);
            }
        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return ResponseEntity.ok(resp);
    }

    @PutMapping("UpdateRoom/{id}")
    public ResponseEntity<ReqRes> updateRoom(@PathVariable Long id, @RequestBody Room room) {
        ReqRes resp = new ReqRes();
        try {
            Room updatedRoom = roomService.updateRoom(id, room);
            if (updatedRoom != null) {
                resp.setRoom(updatedRoom);
                resp.setMessage("Room Updated Successfully");
                resp.setStatusCode(200);
            } else {
                resp.setMessage("Room Not Found");
                resp.setStatusCode(404);
            }
        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return ResponseEntity.ok(resp);
    }
}
