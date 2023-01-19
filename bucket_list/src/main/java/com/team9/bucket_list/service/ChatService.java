package com.team9.bucket_list.service;

import com.team9.bucket_list.domain.dto.chat.ChatRoomResponse;
import com.team9.bucket_list.domain.entity.Chat;
import com.team9.bucket_list.domain.entity.ChatParticipant;
import com.team9.bucket_list.domain.entity.ChatRoom;
import com.team9.bucket_list.repository.ChatParticipantRepository;
import com.team9.bucket_list.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatParticipantRepository chatParticipantRepository;

    public Page<ChatRoomResponse> getChatList(Long memberId, Pageable pageable) {
        Set<ChatParticipant> chatParticipantList = chatParticipantRepository.findAllByMember_Id(memberId);
        Page<ChatRoom> chatRooms = chatRoomRepository.findAllByChatParticipantsIn(chatParticipantList,pageable);
        return null;
    }

}
