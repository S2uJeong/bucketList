package com.team9.bucket_list.domain.dto.chat;

import com.team9.bucket_list.domain.entity.Bucketlist;

import java.util.List;

public class ChatRoomRequest {
    private String roomName;
    private Bucketlist bucketlist;
    private List<Long> memberIds;
}
