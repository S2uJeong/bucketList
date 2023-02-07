package com.team9.bucket_list.domain.dto.chat;

import com.team9.bucket_list.domain.entity.ChatRoom;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Builder
@ToString
public class ChatRoomListResponse {
    Long roomId;
    LocalDateTime lastMessageTime;

    public static ChatRoomListResponse response(ChatRoom chatRoom) {
        return ChatRoomListResponse.builder()
                .roomId(chatRoom.getId())
                .lastMessageTime(chatRoom.getLastMessageTime())
                .build();
    }
}
