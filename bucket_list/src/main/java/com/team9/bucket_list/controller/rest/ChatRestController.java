package com.team9.bucket_list.controller.rest;

import com.team9.bucket_list.domain.Response;
import com.team9.bucket_list.domain.dto.chat.ChatInviteRequest;
import com.team9.bucket_list.domain.dto.chat.ChatRoomRequest;
import com.team9.bucket_list.domain.dto.chat.ChatRoomResponse;
import com.team9.bucket_list.domain.entity.ChatParticipant;
import com.team9.bucket_list.domain.entity.ChatRoom;
import com.team9.bucket_list.service.ChatService;
import io.swagger.v3.oas.annotations.Parameter;
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
public class ChatRestController {

    private final ChatService chatService;

    //유저 채팅목록
    //auth 나중에 추가
    @GetMapping
    public Page<ChatRoomResponse> getChatList(@Parameter(hidden = true) @PageableDefault(size = 20) @SortDefault(sort = "lastMessageTime", direction = Sort.Direction.DESC) Pageable pageable, Authentication authentication) {
        //userId에 해당하는 채팅방 리스트를 가져온다
        return chatService.getChatList(Long.valueOf(authentication.getName()),pageable);
    }

    //채팅방 생성
    @PostMapping("/{bucketlistId}")
    public Response<ChatRoom> createChatRoom(@PathVariable Long bucketlistId, ChatRoomRequest chatRoomRequest) {
        return Response.success(chatService.createChatRoom(bucketlistId, chatRoomRequest));
    }

    //유저 초대
    @PostMapping("/{roomId}")
    public Response<List<ChatParticipant>> inviteMember(@PathVariable Long roomId, ChatInviteRequest chatInviteRequest) {
        return Response.success(chatService.inviteMember(roomId, chatInviteRequest));
    }

    //메시지 내용 불러오기
    //메시지와 로그인된 유저를 불러온다
    @GetMapping("/messages/{roomId}")
    public Response messages(@PathVariable Long roomId, @PageableDefault(size = 20) @SortDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return Response.success(chatService.messages(roomId, pageable));
    }
}
