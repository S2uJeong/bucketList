package com.team9.bucket_list.domain.dto.chat;

import com.team9.bucket_list.domain.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatRoomRequest {
    private String roomName;
    private int totalNum;

    public static ChatRoomRequest save(Post post) {
        return ChatRoomRequest.builder()
                .roomName(post.getTitle())
                .totalNum(post.getEntrantNum())
                .build();
    }
}
