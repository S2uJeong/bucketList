package com.team9.bucket_list.service;

import com.team9.bucket_list.domain.dto.chat.ChatInviteRequest;
import com.team9.bucket_list.domain.dto.chat.ChatRequest;
import com.team9.bucket_list.domain.dto.chat.ChatRoomRequest;
import com.team9.bucket_list.domain.dto.chat.ChatRoomResponse;
import com.team9.bucket_list.domain.entity.*;
import com.team9.bucket_list.repository.ChatParticipantRepository;
import com.team9.bucket_list.repository.ChatRepository;
import com.team9.bucket_list.repository.ChatRoomRepository;
import com.team9.bucket_list.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@EnableAsync
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatParticipantRepository chatParticipantRepository;
    private final MemberRepository memberRepository;
    private final ChatRepository chatRepository;

    public Page<ChatRoomResponse> getChatList(Long memberId, Pageable pageable) {
        List<ChatParticipant> chatParticipantList = chatParticipantRepository.findAllByMember_Id(memberId);
        log.info("서비스 참여 포스트 id" + chatParticipantList.get(0).getId());
        return ChatRoomResponse.pageList(chatRoomRepository.findAllByChatParticipantsIn(chatParticipantList,pageable));
    }

    public void createChatRoom(Long bucketlistId, ChatRoomRequest chatRoomRequest) {
        /*bucketlist repository 나중에 생성*/
        Bucketlist bucketlist = null;
        chatRoomRepository.save(ChatRoom.save(chatRoomRequest,bucketlist));

        //성공 리턴
    }

    public void inviteMember(Long roomId, ChatInviteRequest chatInviteRequest) {
        //멤버 아이디로 멤버 리스트 찾기
        //List<Member> members = memberRepository.findAllbyMemberIdIn(chatInviteRequest.getMemberId());
        List<Member> members = null;

        //이미 방에 들어와있는지 확인

        //roomId로 방 찾기
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(() -> new RuntimeException());

        //해당 멤버와 방 번호를 넣은 chatParticipant list생성 후 저장
        List<ChatParticipant> chatParticipantList =
                members.stream().map(member -> ChatParticipant.builder().chatRoom(chatRoom).member(member).build()).collect(Collectors.toList());

        //chatParticipantList 저장
        chatParticipantRepository.saveAll(chatParticipantList);

        //성공 리턴
    }

    public Page<ChatParticipant> messages(Long roomId, Pageable pageable) {
        return chatParticipantRepository.findAllByChatRoom_Id(roomId,pageable);
    }

    @Async
    public void updateMessage(ChatRequest chatRequest) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRequest.getRoomId()).orElseThrow(() -> new RuntimeException());
        Member member = memberRepository.findById(chatRequest.getUserId()).orElseThrow(() -> new RuntimeException());

        chatRepository.save(Chat.save(chatRequest,chatRoom,member));
        chatRoomRepository.save(ChatRoom.messageTimeUpdate(chatRoom));
    }
}
