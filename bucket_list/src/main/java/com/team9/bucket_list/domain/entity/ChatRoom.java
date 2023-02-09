package com.team9.bucket_list.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.team9.bucket_list.domain.dto.chat.ChatRequest;
import com.team9.bucket_list.domain.dto.chat.ChatRoomRequest;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChatRoom {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_id")
    private Long id;
    private String roomName;
    private int totalNum;
    private LocalDateTime lastMessageTime;
    private String lastMessage;
    private String lastUserName;

    @OneToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @JsonIgnore
    @OneToMany(mappedBy = "chatRoom")
    private final List<Chat> chatList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "chatRoom")
    private final List<ChatParticipant> chatParticipants = new ArrayList<>();


    public static ChatRoom save(ChatRoomRequest chatRoomRequest, Post post) {
        return ChatRoom.builder()
                .roomName(chatRoomRequest.getRoomName())
                .post(post)
                .totalNum(chatRoomRequest.getTotalNum())
                .lastMessage("")
                .lastUserName("")
                .lastMessageTime(LocalDateTime.now())
                .build();
    }

    public static ChatRoom messageTimeUpdate(ChatRoom chatRoom, ChatRequest chatRequest) {
        return chatRoom.builder()
                .id(chatRoom.getId())
                .roomName(chatRoom.getRoomName())
                .totalNum(chatRoom.getTotalNum())
                .post(chatRoom.getPost())
                .lastMessage(chatRequest.getMessage())
                .lastUserName(chatRequest.getUserName())
                .lastMessageTime(LocalDateTime.now())
                .build();
    }
}
