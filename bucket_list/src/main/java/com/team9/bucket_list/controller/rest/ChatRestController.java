package com.team9.bucket_list.controller.rest;

import com.team9.bucket_list.domain.dto.chat.ChatInviteRequest;
import com.team9.bucket_list.domain.dto.chat.ChatRoomRequest;
import com.team9.bucket_list.domain.dto.chat.ChatRoomResponse;
import com.team9.bucket_list.domain.entity.ChatRoom;
import com.team9.bucket_list.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
@Slf4j
public class ChatRestController {

    private final ChatService chatService;

    //유저 채팅목록
    //auth 나중에 추가
    @GetMapping
    public Page<ChatRoomResponse> getChatList(/*auth*/ @PageableDefault(size = 20) /*제일 마지막에 채팅이 올라온 시간이 기준 @SortDefault(sort = "createdAt", direction = Sort.Direction.DESC)*/ Pageable pageable) {
        log.info("컨트롤러");
        Long userId = 1L;
        //userId에 해당하는 채팅방 리스트를 가져온다
        return chatService.getChatList(userId,pageable);
    }

    //채팅방 생성
    @PostMapping("/{bucketlistId}")
    public String createChatRoom(@PathVariable Long bucketlistId, ChatRoomRequest chatRoomRequest) {
        chatService.createChatRoom(bucketlistId, chatRoomRequest);
        return "";
    }

    //유저 초대
    @PostMapping("/{roomId}")
    public String inviteMember(@PathVariable Long roomId, ChatInviteRequest chatInviteRequest) {
        chatService.inviteMember(roomId, chatInviteRequest);
        return "";
    }
}
