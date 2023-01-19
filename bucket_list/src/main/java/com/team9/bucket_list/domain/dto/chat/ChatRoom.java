package com.team9.bucket_list.domain.dto.chat;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Getter
@Setter
@Builder
public class ChatRoom {
    private String roomId; // 채팅방 아이디
    private String name; // 채팅방 이름
    private long userCount; // 채팅방 인원수

    private HashMap<String, String> userlist = new HashMap<String, String>();
    private Set<WebSocketSession> sessions = new HashSet<>();

    public static ChatRoom create(String roomName){
        return ChatRoom.builder()
                .roomId(UUID.randomUUID().toString())
                .name(roomName)
                .build();
    }
}
