package com.team9.bucket_list.controller.rest;

import com.team9.bucket_list.domain.Response;
import com.team9.bucket_list.domain.dto.chat.*;
import com.team9.bucket_list.domain.entity.ChatParticipant;
import com.team9.bucket_list.domain.entity.ChatRoom;
import com.team9.bucket_list.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "채팅", description = "버킷리스트를 함께하는 사람들끼리 채팅방을 통해 소통할 수 있습니다.")
public class ChatRestController {

    private final ChatService chatService;

    //유저 채팅목록
    //auth 나중에 추가
    @GetMapping
    @Operation(summary = "채팅목록 조회", description = "memberId에 해당하는 채팅방 리스트를 출력합니다.")
    public Page<ChatRoomResponse> getChatList(@Parameter(hidden = true) @PageableDefault(size = 20) @SortDefault(sort = "lastMessageTime", direction = Sort.Direction.DESC) Pageable pageable, Authentication authentication) {
        //userId에 해당하는 채팅방 리스트를 가져온다
        return chatService.getChatList(Long.valueOf(authentication.getName()),pageable);
    }

    //채팅방 생성
    @PostMapping("/create-room/{postId}")
    @Operation(summary = "채팅방 생성", description = "채팅방을 생성합니다.")
    public Response<ChatRoom> createChatRoom(@Parameter(name = "postId", description = "게시글 id") @PathVariable Long postId, ChatRoomRequest chatRoomRequest) {
        return Response.success(chatService.createChatRoom(postId, chatRoomRequest));
    }

    //유저 초대
    @PostMapping("/{roomId}")
    @Operation(summary = "멤버 초대", description = "roomId를 이용하여 멤버를 초대합니다.")
    public Response<List<ChatParticipant>> inviteMember(@Parameter(name = "roomId", description = "채팅방 id") @PathVariable Long roomId, ChatInviteRequest chatInviteRequest) {
        return Response.success(chatService.inviteMember(roomId, chatInviteRequest));
    }

    //메시지 내용 불러오기
    @GetMapping("/messages/{roomId}")
    @Operation(summary = "메시지(채팅) 조회", description = "roomId로 조회한 메시지 리스트를 출력합니다.")
    public Response messages(@Parameter(name = "roomId", description = "채팅방 id") @PathVariable Long roomId, @Parameter(hidden = true) @PageableDefault(size = 20) @SortDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return Response.success(chatService.messages(roomId, pageable));
    }

    @GetMapping("/participant/{roomId}")
    @Operation(summary = "채팅방 참가자 조회", description = "roomId로 조회한 메시지 리스트를 출력합니다.")
    public ChatParticipantResponseSuccess getChatParticipant(@Parameter(name = "roomId", description = "채팅방 id") @PathVariable Long roomId) {
        List<ChatParticipant> participants = chatService.getChatParticipant(roomId);
        return ChatParticipantResponseSuccess.success(ChatParticipantResponse.memberList(participants),ChatParticipantResponse.getHost(participants));
    }

    @DeleteMapping("/out")
    @Operation(summary = "채팅방 나가기", description = "roomId와 memberId를 이용해 채팅방을 나갑니다")
    public Response outChatRoom(@RequestBody ChatOutRequest chatOutRequest) {
        return Response.success(chatService.outChatRoom(chatOutRequest));
    }
}
