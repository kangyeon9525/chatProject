package org.example.chatproject.handler;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Log4j2
public class ChatHandler extends TextWebSocketHandler {

    private static List<WebSocketSession> sessions = new ArrayList<>();
    public static Map<String, WebSocketSession> usernameSessionMap = new ConcurrentHashMap<>();

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.info("payload : " + payload);

        for (WebSocketSession sess : sessions) {
            sess.sendMessage(message);
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String username = getUsername(session);

        if (username == null || usernameSessionMap.containsKey(username)) {
            session.sendMessage(new TextMessage("ERROR:닉네임이 이미 사용 중입니다."));
            session.close();
            return;
        }

        sessions.add(session);
        usernameSessionMap.put(username, session);

        log.info(username + " 클라이언트 접속");

        // Notify other users about the new user
        for (WebSocketSession sess : sessions) {
            if (sess.isOpen() && !sess.getId().equals(session.getId())) {
                sess.sendMessage(new TextMessage("(시스템) " + username + " 유저가 입장했습니다."));
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String username = getUsername(session);

        log.info(username + " 클라이언트 접속 해제");

        sessions.remove(session);
        usernameSessionMap.remove(username);

        // Notify other users about the user leaving
        for (WebSocketSession sess : sessions) {
            if (sess.isOpen()) {
                sess.sendMessage(new TextMessage("(시스템) " + username + " 유저가 나갔습니다."));
            }
        }
    }

    private String getUsername(WebSocketSession session) {
        String query = session.getUri().getQuery();
        for (String param : query.split("&")) {
            String[] pair = param.split("=");
            if (pair[0].equals("username")) {
                try {
                    return URLDecoder.decode(pair[1], StandardCharsets.UTF_8.toString());
                } catch (UnsupportedEncodingException e) {
                    log.error("URL 디코딩 오류", e);
                }
            }
        }
        return null;
    }
}







