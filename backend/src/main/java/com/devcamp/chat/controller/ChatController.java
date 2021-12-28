package com.devcamp.chat.controller;

import com.devcamp.chat.model.ChatMessage;
import com.devcamp.chat.model.ChatRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RestController
public class ChatController {

    private final SimpMessageSendingOperations messagingTemplate;

    // pub/chat/message
    // Websocket으로 들어오는 메시지 발행을 처리
    @MessageMapping("/chat/message")
    public void message(ChatMessage message) {
        // 누가 ENTER 하면, 메시지는 입장을 알리는 메시지로 초기화
        if (ChatMessage.MessageType.JOIN.equals(message.getType()))
            message.setMessage(message.getSender() + "님이 입장하셨습니다.");
        // 메시지를 모두에게 보냄
        // 클라이언트에서는 해당 주소를(/sub/chat/room/{roomId}) 구독(subscribe)하고 있다가 메시지가 전달되면 화면에 출력
        messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }
}