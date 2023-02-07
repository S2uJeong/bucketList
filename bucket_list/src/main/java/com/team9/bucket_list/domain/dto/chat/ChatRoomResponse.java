package com.team9.bucket_list.domain.dto.chat;

import com.team9.bucket_list.domain.entity.ChatRoom;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

@Builder
@Data
public class ChatRoomResponse {
    private Long roomId;
    private String roomName;
    private int totalNum;
    private LocalDateTime lastMessageTime;
    private String lastMessage;
    private String lastUserName;

    public static Page<ChatRoomResponse> pageList(Page<ChatRoom> chatRooms) {
        return chatRooms.map(chatRoom -> ChatRoomResponse.builder()
                .roomId(chatRoom.getId())
                .roomName(chatRoom.getRoomName())
                .totalNum(chatRoom.getTotalNum())
                .lastMessageTime(chatRoom.getLastMessageTime())
                .lastMessage(chatRoom.getLastMessage())
                .lastUserName(chatRoom.getLastUserName())
                .build()
        );
    }
}
