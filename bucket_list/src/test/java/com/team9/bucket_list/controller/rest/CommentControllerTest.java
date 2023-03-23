package com.team9.bucket_list.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team9.bucket_list.domain.Response;
import com.team9.bucket_list.domain.dto.comment.CommentCreateRequest;
import com.team9.bucket_list.domain.dto.comment.CommentCreateResponse;
import com.team9.bucket_list.domain.dto.comment.CommentListResponse;
import com.team9.bucket_list.domain.entity.Comment;
import com.team9.bucket_list.domain.entity.Member;
import com.team9.bucket_list.domain.entity.Post;
import com.team9.bucket_list.execption.ApplicationException;
import com.team9.bucket_list.execption.ErrorCode;
import com.team9.bucket_list.fixture.CommentFixture;
import com.team9.bucket_list.fixture.MemberFixture;
import com.team9.bucket_list.fixture.PostFixture;
import com.team9.bucket_list.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentController.class)
class CommentControllerTest {

    Logger log = (Logger) LoggerFactory.getLogger(CommentControllerTest.class);    // Junit에서 log찍기 위해 선언(Junit에서는 @Slf4j 어노테이션 사용 불가능)

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean   // 가짜 객체
    CommentService commentService;

    @MockBean
    BCryptPasswordEncoder encoder;



    @Nested
    @DisplayName("Comment CRUD")
    class CommentCRUD {


        @Test
        @DisplayName("댓글 작성 성공")
        @WithMockUser(username = "1")
        void createComment_success() throws Exception {
            Member member = MemberFixture.get("test@naver.com","1234","test");
            Post post = PostFixture.get(member);

            CommentCreateRequest request = new CommentCreateRequest("content",null);

            Comment comment = request.toEntity(post,null,member);          // 부모댓글 생성


            CommentCreateResponse response = new CommentCreateResponse(comment,member.getUserName());



            given(commentService.commentCreate(any(),any(),any()))
                    .willReturn(response);

            mockMvc.perform(post("/api/v1/comment/1")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(request)))
                    .andDo(print())
                    .andExpect(jsonPath("$..userName").exists())
                    .andExpect(jsonPath("$..id").exists())
                    .andExpect(status().isOk());

            Assertions.assertEquals(response.getParentId(),null);       // 부모댓글 없음(즉, 현재 댓글이 부모댓글이라는 뜻)

            verify(commentService).commentCreate(any(),any(),any());
        }

        @Test
        @DisplayName("대댓글 작성 성공")
        @WithMockUser(username = "1")
        void createReplyComment_success() throws Exception {
            Member member = MemberFixture.get("test@naver.com","1234","test");
            Post post = PostFixture.get(member);

            CommentCreateRequest request = new CommentCreateRequest("content",1l);      // 1l에 대댓글 작성
            
            Comment parentComment = CommentFixture.get(1l,post,null,member,"firstComment");    // 부모 댓글
            
            Comment comment = request.toEntity(post,parentComment,member);          // 대댓글 생성


            CommentCreateResponse response = new CommentCreateResponse(comment,member.getUserName());



            given(commentService.commentCreate(any(),any(),any()))
                    .willReturn(response);

            mockMvc.perform(post("/api/v1/comment/1")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(request)))
                    .andDo(print())
                    .andExpect(jsonPath("$..userName").exists())
                    .andExpect(jsonPath("$..parentId").value(1))       // 부모댓글 존재(부모댓글이 존재한다는 것은 현재 댓글이 대댓글이라는 뜻)
                    .andExpect(jsonPath("$..id").exists())
                    .andExpect(status().isOk());

            verify(commentService).commentCreate(any(),any(),any());
        }

        @Test
        @DisplayName("댓글 작성 실패 - 게시물 없음")
        @WithMockUser(username = "1")
        void createComment_fail_one() throws Exception {
            Member member = MemberFixture.get("test@naver.com","1234","test");

            Post post = null;                   // 해당 게시물이 없을 때

            CommentCreateRequest request = new CommentCreateRequest("content",null);      // 1l에 대댓글 작성


                 given(commentService.commentCreate(any(),any(),any()))
                        .willThrow(new ApplicationException(ErrorCode.POST_NOT_FOUND));


            mockMvc.perform(post("/api/v1/comment/1")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(request)))
                    .andDo(print())
                    .andExpect(status().isNotFound());

            verify(commentService).commentCreate(any(),any(),any());
        }


        @Test
        @DisplayName("댓글 작성 실패 - 유저없음(로그인안함)")
        @WithMockUser(username = "1")
        void createComment_fail_two() throws Exception {
            Member member = null;                       // 로그인을 하지 않아 유저 데이터가 없을때

            CommentCreateRequest request = new CommentCreateRequest("content",null);      // 1l에 대댓글 작성


            given(commentService.commentCreate(any(),any(),any()))
                    .willThrow(new ApplicationException(ErrorCode.USERNAME_NOT_FOUNDED));


            mockMvc.perform(post("/api/v1/comment/1")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(request)))
                    .andDo(print())
                    .andExpect(status().isNotFound());

            verify(commentService).commentCreate(any(),any(),any());
        }


        @Test
        @DisplayName("댓글 리스트 호출 성공")
        @WithMockUser(username = "1")
        void readComment_success() throws Exception {
            Member member = MemberFixture.get("test@naver.com","1234","test");
            Post post = PostFixture.get(member);

            CommentCreateRequest request = new CommentCreateRequest("content",1l);      // 1l에 대댓글 작성

            Comment parentComment = CommentFixture.get(1l,post,null,member,"firstComment");    // 부모 댓글 1번
            Comment replyComment = request.toEntity(post,parentComment,member);          // 대댓글 생성

            Comment parentComment2 = CommentFixture.get(2l,post,null,member,"secondComment");    // 부모 댓글 2번

            CommentListResponse listResponse1 = new CommentListResponse().EntityToDto(parentComment,member.getUserName(),member.getId());
            CommentListResponse listResponse2 = new CommentListResponse().EntityToDto(replyComment,member.getUserName(),member.getId());
            CommentListResponse listResponse3 = new CommentListResponse().EntityToDto(parentComment2,member.getUserName(),member.getId());

            List<CommentListResponse> responses = new ArrayList<>();

            responses.add(listResponse1);
            responses.add(listResponse2);
            responses.add(listResponse3);


            given(commentService.commentList(any()))
                    .willReturn(responses);

            mockMvc.perform(get("/api/v1/comment/1/list")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(request)))
                    .andDo(print())
                    .andExpect(status().isOk());


            verify(commentService).commentList(any());
        }



        @Test
        @DisplayName("댓글 수정 성공")
        @WithMockUser(username = "1")
        void updateComment_success() throws Exception {
            Member member = MemberFixture.get("test@naver.com","1234","test");
            Post post = PostFixture.get(member);

            CommentCreateRequest request = new CommentCreateRequest("updatecontent",null);

            Comment comment = CommentFixture.get(1l,post,null,member,"firstComment");    // 부모 댓글 1번

            comment.update(request.getContent());               // 데이터 업데이트

            List<CommentListResponse> responses = new ArrayList<>();

            CommentListResponse listResponse = new CommentListResponse().EntityToDto(comment,member.getUserName(),member.getId());

            responses.add(listResponse);

            given(commentService.updateComment(any(),any(),any(),any()))
                    .willReturn(responses);

            mockMvc.perform(put("/api/v1/comment/1/update/1")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(request)))
                    .andDo(print())
                    .andExpect(jsonPath("$..content").value("updatecontent"))
                    .andExpect(status().isOk());

            verify(commentService).updateComment(any(),any(),any(),any());
        }

        @Test
        @DisplayName("댓글 수정 실패 - 포스트 없음")
        @WithMockUser(username = "1")
        void updateComment_fail_one() throws Exception {
            Member member = MemberFixture.get("test@naver.com","1234","test");
            Post post = null;

            CommentCreateRequest request = new CommentCreateRequest("updatecontent",null);


            given(commentService.updateComment(any(),any(),any(),any()))
                    .willThrow(new ApplicationException(ErrorCode.POST_NOT_FOUND));

            mockMvc.perform(put("/api/v1/comment/1/update/1")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(request)))
                    .andDo(print())
                    .andExpect(status().isNotFound());

            verify(commentService).updateComment(any(),any(),any(),any());
        }

        @Test
        @DisplayName("댓글 수정 실패 - 유저 불일치")
        @WithMockUser(username = "1")
        void updateComment_fail_two() throws Exception {
            Member member = MemberFixture.get("test@naver.com","1234","test");      // 댓글 작성 유저
            Member member2 = MemberFixture.get("test2@naver.com","1234","test2");   // 댓글 수정하는 유저
            Post post = PostFixture.get(member);

            CommentCreateRequest request = new CommentCreateRequest("updatecontent",null);

            Comment comment = CommentFixture.get(1l,post,null,member,"firstComment");    // 부모 댓글 1번

            comment.update(request.getContent());               // 데이터 업데이트

            List<CommentListResponse> responses = new ArrayList<>();

            CommentListResponse listResponse = new CommentListResponse().EntityToDto(comment,member.getUserName(),member.getId());

            responses.add(listResponse);

            if(!member.getUserName().equals(member2.getUserName())){
                given(commentService.updateComment(any(),any(),any(),any()))
                        .willThrow(new ApplicationException(ErrorCode.USERNAME_NOT_FOUNDED));
            }else{
                given(commentService.updateComment(any(),any(),any(),any()))
                        .willReturn(responses);
            }


            mockMvc.perform(put("/api/v1/comment/1/update/1")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(request)))
                    .andDo(print())
                    .andExpect(status().isNotFound());

            verify(commentService).updateComment(any(),any(),any(),any());
        }

        @Test
        @DisplayName("댓글 삭제 성공")
        @WithMockUser(username = "1")
        void deleteComment_success() throws Exception {
            mockMvc.perform(delete("/api/v1/comment/1/update/1")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk());
        }
    }

}