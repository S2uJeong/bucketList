package com.team9.bucket_list.domain.dto.chat;

import com.team9.bucket_list.domain.entity.Bucketlist;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomRequest {
    private String roomName;
    private int totalNum;
}
