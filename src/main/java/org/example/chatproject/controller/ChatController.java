package org.example.chatproject.controller;

import lombok.extern.log4j.Log4j2;
import org.example.chatproject.handler.ChatHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
@Log4j2
public class ChatController {

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @PostMapping("/chat")
    public String chat(@RequestParam("username") String username) {
        // 이미 사용 중인 닉네임인지 확인
        if (ChatHandler.usernameSessionMap.containsKey(username)) {
            return "redirect:/?error=true";
        }
        try {
            // URL 인코딩
            String encodedUsername = URLEncoder.encode(username, StandardCharsets.UTF_8.toString());
            return "redirect:/chat?username=" + encodedUsername;
        } catch (UnsupportedEncodingException e) {
            log.error("URL 인코딩 오류", e);
            return "redirect:/?error=true";
        }
    }

    ///chat 엔드포인트인 컨트롤러
    @GetMapping("/chat")
    public String chatGET() {
        log.info("@ChatController.chatGET()");
        return "chat"; //리턴으로 템플릿엔진 chat.html 을 반환하도록 설정
    }

}


