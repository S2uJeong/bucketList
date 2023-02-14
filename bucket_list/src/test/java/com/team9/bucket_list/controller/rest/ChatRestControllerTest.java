package com.team9.bucket_list.controller.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team9.bucket_list.domain.Response;
import com.team9.bucket_list.domain.dto.chat.ChatOutRequest;
import com.team9.bucket_list.domain.dto.chat.ChatRoomRequest;
import com.team9.bucket_list.domain.entity.Chat;
import com.team9.bucket_list.domain.entity.ChatParticipant;
import com.team9.bucket_list.domain.entity.ChatRoom;
import com.team9.bucket_list.domain.entity.Member;
import com.team9.bucket_list.execption.ApplicationException;
import com.team9.bucket_list.execption.ErrorCode;
import com.team9.bucket_list.fixture.MemberFixture;
import com.team9.bucket_list.fixture.PostFixture;
import com.team9.bucket_list.service.ChatService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChatRestController.class)
class ChatRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ChatService chatService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("채팅방 리스트 가져오기 성공")
    @WithMockUser(username="1")
    void getChatList() throws Exception {
        mockMvc.perform(get("/chat")
                        .with(csrf())
                        .param("page","0")
                        .param("size","10")
                        .param("sort","lastMessageTime,desc"))
                .andExpect(status().isOk());

        ArgumentCaptor<Pageable> pageableArg = ArgumentCaptor.forClass(Pageable.class);

        verify(chatService).getChatList(any(),pageableArg.capture());
        PageRequest pageable = (PageRequest) pageableArg.getValue();

        assertEquals(0,pageable.getPageNumber());
        assertEquals(10,pageable.getPageSize());
        assertEquals(Sort.by("lastMessageTime","desc"),pageable.withSort(Sort.by("lastMessageTime","desc")).getSort());
    }

    @Test
    @DisplayName("채팅방 리스트 가져오기 실패 - 로그인")
    void getChatList_fail() throws Exception {
        mockMvc.perform(get("/chat")
                        .with(csrf())
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "lastMessageTime,desc"))
                .andExpect(status().isFound());
    }

    @Test
    @DisplayName("채팅 메시지 불러오기 성공")
    @WithMockUser(username="1")
    void messages() throws Exception {
        mockMvc.perform(get("/chat/messages/1")
                        .with(csrf())
                        .param("page","0")
                        .param("size","10")
                        .param("sort","createdAt,desc"))
                .andExpect(status().isOk());

        ArgumentCaptor<Pageable> pageableArg = ArgumentCaptor.forClass(Pageable.class);

        verify(chatService).messages(any(),pageableArg.capture());
        PageRequest pageable = (PageRequest) pageableArg.getValue();

        assertEquals(0,pageable.getPageNumber());
        assertEquals(10,pageable.getPageSize());
        assertEquals(Sort.by("createdAt","desc"),pageable.withSort(Sort.by("createdAt","desc")).getSort());
    }

    @Test
    @DisplayName("채팅 메시지 불러오기 실패 - 로그인")
    void messages_fail() throws Exception {
        mockMvc.perform(get("/chat/messages/1")
                        .with(csrf())
                        .param("page","0")
                        .param("size","10")
                        .param("sort","createdAt,desc"))
                .andExpect(status().isFound());
    }

    @Test
    @DisplayName("채팅방 참가자 조회 성공")
    @WithMockUser(username="1")
    void getChatParticipant() throws Exception {
        ChatRoom chatRoom = ChatRoom.builder().id(1L).roomName("test").totalNum(1).lastMessage("").lastUserName("").lastMessageTime(LocalDateTime.now()).post(PostFixture.get()).build();
        Member member = MemberFixture.get("test","test","test");

        ChatParticipant chatParticipant = ChatParticipant.builder().id(1L).chatRoom(chatRoom).member(member).build();

        List<ChatParticipant> list = new ArrayList<>();
        list.add(chatParticipant);
        list.add(chatParticipant);

        when(chatService.getChatParticipant(any())).thenReturn(list);

        mockMvc.perform(get("/chat/participant/1")
                .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("채팅방 참가자 조회 실패 - 로그인")
    void getChatParticipant_fail() throws Exception {
        ChatRoom chatRoom = ChatRoom.builder().id(1L).roomName("test").totalNum(1).lastMessage("").lastUserName("").lastMessageTime(LocalDateTime.now()).post(PostFixture.get()).build();
        Member member = MemberFixture.get("test","test","test");

        ChatParticipant chatParticipant = ChatParticipant.builder().id(1L).chatRoom(chatRoom).member(member).build();

        List<ChatParticipant> list = new ArrayList<>();
        list.add(chatParticipant);
        list.add(chatParticipant);

        when(chatService.getChatParticipant(any())).thenReturn(list);

        mockMvc.perform(get("/chat/participant/1")
                        .with(csrf()))
                .andExpect(status().isFound());
    }

    @Test
    @DisplayName("채팅방 나가기 성공")
    @WithMockUser(username="1")
    void outChatRoom() throws Exception {
        ChatOutRequest chatOutRequest = new ChatOutRequest(1L,1L);

        when(chatService.outChatRoom(chatOutRequest)).thenReturn(1);

        mockMvc.perform(delete("/chat/out")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(chatOutRequest)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("채팅방 나가기 실패 로그인")
    void outChatRoom_fail() throws Exception {
        ChatOutRequest chatOutRequest = new ChatOutRequest(1L,1L);

        when(chatService.outChatRoom(chatOutRequest)).thenReturn(1);

        mockMvc.perform(delete("/chat/out")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(chatOutRequest)))
                .andExpect(status().isFound());
    }

    @Test
    @DisplayName("채팅방 나가기 실패 채팅방 없음")
    @WithMockUser(username="1")
    void outChatRoom_fail2() throws Exception {
        ChatOutRequest chatOutRequest = new ChatOutRequest(1L,1L);

        when(chatService.outChatRoom(chatOutRequest)).thenThrow(new ApplicationException(ErrorCode.CHAT_ROOM_NOT_FOUND));

        mockMvc.perform(delete("/chat/out")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(chatOutRequest)))
                .andExpect(status().isNotFound());
    }
}