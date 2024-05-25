// src/main/java/com/example/chat/service/RoomService.java
package org.example.chatProject.service;

import org.example.chatProject.model.Room;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RoomService {
    private final ConcurrentHashMap<String, Room> rooms = new ConcurrentHashMap<>();

    public Collection<Room> getAllRooms() {
        return rooms.values();
    }

    public Room getRoom(String name) {
        return rooms.get(name);
    }

    public Room createRoom(String name) {
        Room room = new Room(name);
        rooms.put(name, room);
        return room;
    }

    public void deleteRoom(String name) {
        rooms.remove(name);
    }
}
