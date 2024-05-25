// src/main/java/com/example/chat/controller/RoomController.java
package org.example.chatProject.controller;

import org.example.chatProject.model.Room;
import org.example.chatProject.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @GetMapping
    public Collection<Room> getAllRooms() {
        return roomService.getAllRooms();
    }

    @PostMapping
    public Room createRoom(@RequestParam String name) {
        return roomService.createRoom(name);
    }

    @GetMapping("/{name}")
    public Room getRoom(@PathVariable String name) {
        return roomService.getRoom(name);
    }

    @DeleteMapping("/{name}")
    public void deleteRoom(@PathVariable String name) {
        roomService.deleteRoom(name);
    }
}
