package com.team9.bucket_list.domain.dto.chat;

import com.team9.bucket_list.domain.entity.Chat;
import com.team9.bucket_list.domain.enumerate.ChatType;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

@Builder
@Getter
public class ChatMessagesResponse {
    private Long id;
    private String message;
    private ChatType chatType;
    private String createdAt;
    private String userName;
    private Long memberId;

    public static Page<ChatMessagesResponse> messageList(Page<Chat> chats) {
        return chats.map(chat -> ChatMessagesResponse.builder()
                .id(chat.getId())
                .message(chat.getMessage())
                .chatType(chat.getChatType())
                .createdAt(chat.getCreatedAt())
                .userName(chat.getMember().getUserName())
                .memberId(chat.getMember().getId())
                .build());
    }
}
