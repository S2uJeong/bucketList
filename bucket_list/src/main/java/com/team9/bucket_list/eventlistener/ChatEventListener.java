package com.team9.bucket_list.eventlistener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class ChatEventListener {

    private final SimpMessageSendingOperations messagingTemplate;

    @EventListener
    public void handleWebScoketConnectListener(SessionConnectedEvent event) {
        log.info("새로운 웹 소켓 연결");
    }

    /*@EventListener
    public void handleWebSocketDisconnectListener(SessionConnectedEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        String username = (String) headerAccessor.getSessionAttributes().get("username");

        if(username != null) {
            log.info(String.format("%s님이 퇴장했습니다"),username);

            Chat chat = new Chat();
            chat.setType(Chat.MessageType.LEAVE);
            chat.setSender(username);

            messagingTemplate.convertAndSend("/topic/public",chat);
        }
    }*/
}
