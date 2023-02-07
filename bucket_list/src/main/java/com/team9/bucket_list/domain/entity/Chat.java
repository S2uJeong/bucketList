package com.team9.bucket_list.domain.entity;

import com.team9.bucket_list.domain.dto.chat.ChatRequest;
import com.team9.bucket_list.domain.enumerate.ChatType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Chat extends BaseTimeEntity{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id")
    private Long id;
    private String message;
    private ChatType chatType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public static Chat save(ChatRequest chatRequest, ChatRoom chatRoom, Member member) {
        return Chat.builder()
                .message(chatRequest.getMessage())
                .chatType(chatRequest.getChatType())
                .chatRoom(chatRoom)
                .member(member)
                .build();
    }
}
