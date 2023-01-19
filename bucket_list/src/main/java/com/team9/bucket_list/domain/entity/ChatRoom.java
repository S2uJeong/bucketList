package com.team9.bucket_list.domain.entity;

import com.team9.bucket_list.domain.dto.chat.ChatRoomRequest;
import jakarta.persistence.*;
import lombok.*;

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
    @OneToOne
    @JoinColumn(name = "bucketlist_id")
    private Bucketlist bucketlist;

    @OneToMany(mappedBy = "chatRoom")
    private List<Chat> chatList = new ArrayList<>();

    @OneToMany(mappedBy = "chatRoom")
    private List<ChatParticipant> chatParticipants = new ArrayList<>();


    public static ChatRoom save(ChatRoomRequest chatRoomRequest, Bucketlist bucketlist) {
        return ChatRoom.builder()
                .roomName(chatRoomRequest.getRoomName())
                .bucketlist(bucketlist)
                .totalNum(chatRoomRequest.getTotalNum())
                .build();
    }
}
