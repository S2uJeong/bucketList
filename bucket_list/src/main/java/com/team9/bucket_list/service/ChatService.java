package com.team9.bucket_list.service;

import com.team9.bucket_list.domain.dto.application.ApplicationDecisionRequest;
import com.team9.bucket_list.domain.dto.chat.*;
import com.team9.bucket_list.domain.entity.*;
import com.team9.bucket_list.domain.enumerate.PostStatus;
import com.team9.bucket_list.execption.ApplicationException;
import com.team9.bucket_list.execption.ErrorCode;
import com.team9.bucket_list.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@EnableAsync
@Transactional(readOnly = true)
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatParticipantRepository chatParticipantRepository;
    private final MemberRepository memberRepository;
    private final ChatRepository chatRepository;
    private final PostRepository postRepository;
    private final ApplicationRepository applicationRepository;

    @Transactional
    public Page<ChatRoomResponse> getChatList(Long memberId, Pageable pageable) {
        List<ChatParticipant> chatParticipantList = chatParticipantRepository.findAllByMember_Id(memberId);
        log.info("서비스 참여 포스트 id" + chatParticipantList.get(0).getId());
        return ChatRoomResponse.pageList(chatRoomRepository.findAllByChatParticipantsIn(chatParticipantList,pageable));
    }

    @Transactional
    public ChatRoom createChatRoom(Post post) {
        //postId로 post 엔티티 찾기
        //Post post = postRepository.findById(postId).orElseThrow(() -> new ApplicationException(ErrorCode.POST_NOT_FOUND));

        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.save(post));

        //성공 리턴
        return chatRoom;
    }

    @Transactional
    public ChatRoom createChatRoom(Long postId, ChatRoomRequest chatRoomRequest) {
        //postId로 post 엔티티 찾기
        Post post = postRepository.findById(postId).orElseThrow(() -> new ApplicationException(ErrorCode.POST_NOT_FOUND));

        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.save(post));

        //성공 리턴
        return chatRoom;
    }

    @Transactional
    public List<ChatParticipant> inviteMember(Long roomId, ChatInviteRequest chatInviteRequest) {
        //멤버 아이디로 멤버 리스트 찾기
        List<Member> members = memberRepository.findAllMemberIdIn(chatInviteRequest.getMembersId());

        //이미 방에 들어와있는지 확인
        for(Member member : members) {
            Optional<ChatParticipant> findMember = chatParticipantRepository.findByChatRoom_IdAndMember_Id(roomId,member.getId());
            if(findMember.isPresent()) members.remove(member);
        }

        //roomId로 방 찾기
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(() -> new RuntimeException());

        //해당 멤버와 방 번호를 넣은 chatParticipant list생성 후 저장
        List<ChatParticipant> chatParticipantList =
                members.stream().map(member -> ChatParticipant.builder().chatRoom(chatRoom).member(member).build()).collect(Collectors.toList());

        //chatParticipantList 저장
        chatParticipantRepository.saveAll(chatParticipantList);

        //성공 리턴
        return chatParticipantList;
    }

    @Transactional
    public Page<ChatMessagesResponse> messages(Long roomId, Pageable pageable) {
        return ChatMessagesResponse.messageList(chatRepository.findAllByChatRoom_Id(roomId,pageable));
    }

    @Async
    @Transactional
    public CompletableFuture<ChatRequest> updateMessage(ChatRequest chatRequest) {
        log.info(chatRequest.toString());
        //채팅방이 존재하는지, 멤버가 존재하는지 확인
        ChatRoom chatRoom = chatRoomRepository.findById(chatRequest.getRoomId()).orElseThrow(() -> new ApplicationException(ErrorCode.INVALID_PERMISSION));
        Member member = memberRepository.findById(chatRequest.getMemberId()).orElseThrow(() -> new ApplicationException(ErrorCode.USERNAME_NOT_FOUNDED));

        //채팅저장및 채팅방 업데이트 시간 저장
        chatRepository.save(Chat.save(chatRequest,chatRoom,member));
        ChatRoom savedChatRoom = chatRoomRepository.save(ChatRoom.messageTimeUpdate(chatRoom,chatRequest));
        log.info("채팅 저장 완료");

        return CompletableFuture.completedFuture(ChatRequest.chatListResponse(savedChatRoom));
    }

    @Transactional
    public void checkParticipant(ChatRequest chatRequest) {
        log.info("해당 참가자가 채팅방에 들어오는게 허용된 참가자인지 확인");
        chatParticipantRepository.findByChatRoom_IdAndMember_Id(chatRequest.getRoomId(), chatRequest.getMemberId())
                .orElseThrow(() -> new ApplicationException(ErrorCode.INVALID_PERMISSION));
        log.info("채팅방에 등록된 사용자 확인완료");
    }

    @Transactional
    public List<ChatParticipant> getChatParticipant(Long roomId) {
        return chatParticipantRepository.findAllByChatRoom_Id(roomId);
    }

    @Transactional
    public int outChatRoom(ChatOutRequest chatOutRequest) throws ParseException {
        ChatRoom chatRoom = chatRoomRepository.findById(chatOutRequest.getRoomId()).orElseThrow(() -> new ApplicationException(ErrorCode.CHAT_ROOM_NOT_FOUND));
        ChatParticipant chatParticipant = chatParticipantRepository.findByChatRoom_IdAndMember_Id(chatOutRequest.getRoomId(), chatOutRequest.getMemberId()).orElseThrow(() -> new ApplicationException(ErrorCode.CHAT_ROOM_NOT_FOUND));
        //Application application = applicationRepository.findByMember_IdAndPost_Id(chatOutRequest.getMemberId(), chatRoom.getPost().getId()).orElseThrow(() -> new ApplicationException(ErrorCode.APPLICATION_NOT_FOUND));

        //채팅방 퇴장
        log.info(chatOutRequest.getMemberId() + ", " + chatOutRequest.getRoomId());
        log.info("채팅방 나가기");
        chatParticipantRepository.deleteById(chatParticipant.getId());
        //신청서 거절로 변경

        log.info("신청서 거절로 변경");
        /*ApplicationDecisionRequest applicationDecisionRequest = ApplicationDecisionRequest.builder().status((byte) 2).build();
        applicationRepository.save(Application.updateStatus(application,applicationDecisionRequest));*/
        applicationRepository.updateRejectApplication(chatOutRequest.getMemberId(), chatRoom.getPost().getId());

        //현재 시각이 모집 마감일 전이면 모집중으로 변경
        Post post = chatRoom.getPost();

        String untilRecruit = post.getUntilRecruit();

        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        Date recruitEndDate = formatter.parse(untilRecruit);
        Date now = new Date();

        if (now.before(recruitEndDate)) {
            //모집 마감일 전
            post.setPostStatusJoin();
        }

        return 1;
    }
}
