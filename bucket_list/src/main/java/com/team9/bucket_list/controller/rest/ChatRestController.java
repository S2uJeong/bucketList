package com.team9.bucket_list.controller.rest;

import com.team9.bucket_list.domain.dto.chat.ChatRoomRequest;
import com.team9.bucket_list.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("chat")
@RequiredArgsConstructor
@Slf4j
public class ChatRestController {

    private final ChatService chatService;

    //유저 채팅목록
    //auth 나중에 추가
    @GetMapping("/")
    public String getChatList(/*auth*/ ) {
        Long userId = 1L;
        //userId에 해당하는 채팅 리스트를 가져온다
        return "";
    }

    //채팅방 생성
    @PostMapping("/")
    public String createChatRoom(ChatRoomRequest chatRoomRequest) {
        return "";
    }

    //유저 초대
}
