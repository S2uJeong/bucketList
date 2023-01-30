package com.team9.bucket_list.domain.dto.chat;

import com.team9.bucket_list.domain.enumerate.ChatType;
import lombok.Getter;

@Getter
public class ChatRequest {
    private Long roomId;
    private Long userId;
    private String userName;
    private ChatType chatType;
    private String message;
}
