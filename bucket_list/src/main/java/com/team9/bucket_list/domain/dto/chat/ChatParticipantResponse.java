package com.team9.bucket_list.domain.dto.chat;

import com.team9.bucket_list.domain.entity.Chat;
import com.team9.bucket_list.domain.entity.ChatParticipant;
import com.team9.bucket_list.domain.entity.Member;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Builder
@Getter
public class ChatParticipantResponse {
    Long memberId;
    String userName;

    public static List<ChatParticipantResponse> memberList(List<ChatParticipant> participants) {
        return participants.stream().map(participant -> ChatParticipantResponse.builder()
                .memberId(participant.getMember().getId())
                .userName(participant.getMember().getUserName())
                .build()).toList();
    }

    public static ChatParticipantResponse getHost(List<ChatParticipant> participants) {
        return ChatParticipantResponse.builder()
                .memberId(participants.get(0).getChatRoom().getPost().getId())
                .userName(participants.get(0).getChatRoom().getPost().getMember().getUserName())
                .build();
    }
}
