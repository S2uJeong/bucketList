package com.team9.bucket_list.domain.dto.chat;

import com.team9.bucket_list.domain.enumerate.ChatType;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ChatRequest {
    private Long roomId;
    private Long memberId;
    private String userName;
    private ChatType chatType;
    private String message;
}
