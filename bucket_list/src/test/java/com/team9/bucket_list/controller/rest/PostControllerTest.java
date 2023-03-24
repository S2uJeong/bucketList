package com.team9.bucket_list.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team9.bucket_list.fixture.dto.*;
import com.team9.bucket_list.fixture.MemberFixture;
import com.team9.bucket_list.domain.dto.post.*;
import com.team9.bucket_list.domain.entity.Member;
import com.team9.bucket_list.domain.entity.Post;
import com.team9.bucket_list.execption.ApplicationException;
import com.team9.bucket_list.execption.ErrorCode;
import com.team9.bucket_list.fixture.PostFixture;
import com.team9.bucket_list.service.PostService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostRestController.class)
class PostControllerTest {

    Logger log = (Logger) LoggerFactory.getLogger(PostControllerTest.class);    // Junit에서 log찍기 위해 선언(Junit에서는 @Slf4j 어노테이션 사용 불가능)

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean   // 가짜 객체
    PostService postService;

    @MockBean
    BCryptPasswordEncoder encoder;


    @Nested
    @DisplayName("Post CRUD")
    class PostCRUD {

        @Test
        @DisplayName("포스트 작성 성공")
        @WithMockUser(username = "1")
        void createPost_success() throws Exception {
            Member member = MemberFixture.get("test@naver.com","1234","test");

            PostCreateRequest request = PostCreateRequestFixture.get();

            Post post = PostFixture.get(member);

            PostCreateResponse createResponse = PostCreateResponseFixture.get(post);


            given(postService.create(any(),any()))
                    .willReturn(createResponse);

            mockMvc.perform(post("/post/detailpost")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(request)))
                    .andDo(print())
                    .andExpect(jsonPath("$..postId").exists())
                    .andExpect(status().isOk());

            verify(postService).create(any(),any());
        }

        @Test
        @DisplayName("포스트 작성 실패 - 로그인을 하지 않음")
        @WithMockUser(username = "1")
        void createPost_fail() throws Exception {
            PostCreateRequest request = new PostCreateRequest("title", "02/15/2023", "02/21/2023", "02/25/2023", "TRAVEL", 5, 10000, "서울 시청", "content");
            PostCreateResponse createResponse = new PostCreateResponse(1l, "post 등록완료");

            given(postService.create(any(), any()))
                    .willThrow(new ApplicationException(ErrorCode.INVALID_PERMISSION));

            mockMvc.perform(post("/post/detailpost")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(request)))
                    .andDo(print())
                    .andExpect(status().isUnauthorized());

            verify(postService).create(any(), any());
        }

        @Test
        @DisplayName("포스트 세부 조회 성공")
        @WithMockUser
        void readPost_success() throws Exception {

            Member member = MemberFixture.get("test@naver.com","1234","test");

            Post post = PostFixture.get(member);


            Map<String, Double> locationNum = postService.findLoction(post.getLocation());
            Double lat = locationNum.get("lat");
            Double lng = locationNum.get("lng");

            PostReadResponse response = PostReadResponseFixture.get(post,lat,lng);

            given(postService.read(1l))
                    .willReturn(response);

            mockMvc.perform(get("/post/1/json")
                            .with(csrf()))
                    .andDo(print())
                    .andExpect(jsonPath("$..userName").value("test"))
                    .andExpect(jsonPath("$..lat").exists())
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("포스트 세부 조회 실패 - 포스트 없음")
        @WithMockUser
        void readPost_fail_one() throws Exception {

            given(postService.read(any()))
                    .willThrow(new ApplicationException(ErrorCode.POST_NOT_FOUND));

            mockMvc.perform(get("/post/1/json")
                            .with(csrf()))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }


        @Test
        @DisplayName("포스트 수정 성공")
        @WithMockUser(username = "1")
        void updatePost_success() throws Exception {

            PostUpdateRequest request = PostUpdateRequestFixture.get();


            PostUpdateResponse response = PostUpdateResponseFixture.get("updateTitle","updateContent");

            given(postService.update(any(),any(),any()))
                    .willReturn(response);

            mockMvc.perform(put("/post/1/update")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(request)))
                    .andDo(print())
                    .andExpect(jsonPath("$..title").value("updateTitle"))
                    .andExpect(jsonPath("$..content").exists())
                    .andExpect(status().isOk());

            verify(postService).update(any(),any(),any());
        }

        @Test
        @DisplayName("포스트 수정 실패 - 포스트 없음")
        @WithMockUser(username = "1")
        void updatePost_fail_one() throws Exception {

            PostUpdateRequest request = PostUpdateRequestFixture.get();

            given(postService.update(any(), any(), any()))
                    .willThrow(new ApplicationException(ErrorCode.POST_NOT_FOUND));

            mockMvc.perform(put("/post/1/update")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(request)))
                    .andDo(print())
                    .andExpect(status().isNotFound());

            verify(postService).update(any(), any(), any());
        }

        @Test
        @DisplayName("포스트 수정 실패 - 유저 일치 안됨")
        @WithMockUser(username = "1")
        void updatePost_fail_two() throws Exception {

            PostUpdateRequest request = PostUpdateRequestFixture.get();


            given(postService.update(any(), any(), any()))
                    .willThrow(new ApplicationException(ErrorCode.USERNAME_NOT_FOUNDED));

            mockMvc.perform(put("/post/1/update")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(request)))
                    .andDo(print())
                    .andExpect(status().isNotFound());

            verify(postService).update(any(), any(), any());
        }


        @Test
        @DisplayName("포스트 삭제 성공")
        @WithMockUser(username = "1")
        void deletePost_success() throws Exception {

            mockMvc.perform(delete("/post/1")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk());
        }
    }
}