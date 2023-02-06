package com.team9.bucket_list.controller.rest;

import com.team9.bucket_list.domain.Response;
import com.team9.bucket_list.domain.dto.chat.ChatInviteRequest;
import com.team9.bucket_list.domain.dto.chat.ChatMessageResponse;
import com.team9.bucket_list.domain.dto.chat.ChatRoomRequest;
import com.team9.bucket_list.domain.dto.chat.ChatRoomResponse;
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
import org.springframework.data.web.PageableDefault;
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
    public Page<ChatRoomResponse> getChatList(/*auth*/@Parameter(hidden = true) @PageableDefault(size = 20) /*제일 마지막에 채팅이 올라온 시간이 기준 @SortDefault(sort = "createdAt", direction = Sort.Direction.DESC)*/ Pageable pageable) {
        log.info("컨트롤러");
        Long userId = 1L;
        //userId에 해당하는 채팅방 리스트를 가져온다
        return chatService.getChatList(userId,pageable);
    }

    //채팅방 생성
    @PostMapping("/{bucketlistId}")
    @Operation(summary = "채팅방 생성", description = "채팅방을 생성합니다.")
    public Response<ChatRoom> createChatRoom(@Parameter(name = "bucketlistId", description = "버킷리스트 id") @PathVariable Long bucketlistId,
                                             @RequestBody ChatRoomRequest chatRoomRequest) {
        return Response.success(chatService.createChatRoom(bucketlistId, chatRoomRequest));
    }

    //유저 초대
    @PostMapping("/{roomId}")
    @Operation(summary = "멤버 초대", description = "roomId를 이용하여 멤버를 초대합니다.")
    public Response<List<ChatParticipant>> inviteMember(@Parameter(name = "roomId", description = "채팅방 id") @PathVariable Long roomId,
                                                        @RequestBody ChatInviteRequest chatInviteRequest) {
        return Response.success(chatService.inviteMember(roomId, chatInviteRequest));
    }

    //메시지 내용 불러오기
    //메시지와 로그인된 유저를 불러온다
    @GetMapping("/messages/{roomId}")
    @Operation(summary = "메시지(채팅) 조회", description = "roomId로 조회한 메시지 리스트를 출력합니다.")
    public ChatMessageResponse messages(@Parameter(name = "roomId", description = "채팅방 id") @PathVariable Long roomId,
                                        @Parameter(hidden = true) @PageableDefault(size = 20) Pageable pageable) {
        String userName = "test1";
        return ChatMessageResponse.success(chatService.messages(roomId, pageable), userName);
    }
}
