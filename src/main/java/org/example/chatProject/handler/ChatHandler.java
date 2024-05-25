// src/main/java/com/example/chat/handler/ChatHandler.java
package org.example.chatProject.handler;

import org.example.chatProject.model.Room;
import org.example.chatProject.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;


@Component
public class ChatHandler extends TextWebSocketHandler {

    @Autowired
    private RoomService roomService;

    private ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String roomName = getRoomName(session);
        Room room = roomService.getRoom(roomName);
        if (room != null) {
            room.addUser(session.getId());
            sessions.put(session.getId(), session);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String roomName = getRoomName(session);
        Room room = roomService.getRoom(roomName);
        if (room != null) {
            for (String userId : room.getUsers()) {
                WebSocketSession userSession = sessions.get(userId);
                if (userSession != null && userSession.isOpen()) {
                    try {
                        userSession.sendMessage(message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String roomName = getRoomName(session);
        Room room = roomService.getRoom(roomName);
        if (room != null) {
            room.removeUser(session.getId());
            sessions.remove(session.getId());
        }
    }

    private String getRoomName(WebSocketSession session) {
        String path = session.getUri().getPath();
        return path.substring(path.lastIndexOf('/') + 1);
    }
}
