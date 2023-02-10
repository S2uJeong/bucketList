package com.team9.bucket_list.domain.dto.chat;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
@Data
public class ChatParticipantResponseSuccess {
    private List<ChatParticipantResponse> result;
    private ChatParticipantResponse host;

    public static ChatParticipantResponseSuccess success(List<ChatParticipantResponse> result, ChatParticipantResponse host) {
        return ChatParticipantResponseSuccess.builder()
                .result(result)
                .host(host)
                .build();
    }
}
