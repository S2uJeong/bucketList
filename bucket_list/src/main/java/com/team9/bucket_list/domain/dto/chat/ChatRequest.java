package com.team9.bucket_list.domain.dto.chat;

import com.team9.bucket_list.domain.entity.ChatRoom;
import com.team9.bucket_list.domain.enumerate.ChatType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatRequest {
    private Long roomId;
    private Long memberId;
    private String userName;
    private ChatType chatType;
    private String message;
    private LocalDateTime lastModifiedAt;

    public ChatRequest(Long roomId, Long memberId, String userName, ChatType chatType) {
        this.roomId = roomId;
        this.memberId = memberId;
        this.userName = userName;
        this.chatType = chatType;
    }

    public ChatRequest(Long roomId, Long memberId, String userName, ChatType chatType, String message) {
        this.roomId = roomId;
        this.memberId = memberId;
        this.userName = userName;
        this.chatType = chatType;
        this.message = message;
    }

    public static ChatRequest chatListResponse(ChatRoom chatRoom) {
        return ChatRequest.builder()
                .roomId(chatRoom.getId())
                .lastModifiedAt(chatRoom.getLastMessageTime())
                .chatType(ChatType.LIST)
                .build();
    }
}
