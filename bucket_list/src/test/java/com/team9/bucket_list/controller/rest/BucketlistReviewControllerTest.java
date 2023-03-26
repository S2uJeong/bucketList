//package com.team9.bucket_list.controller.rest;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.team9.bucket_list.domain.dto.bucketlistReview.BucketlistReviewRequest;
//import com.team9.bucket_list.service.BucketlistReviewService;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(BucketlistReviewController.class)
//@MockBean(JpaMetamodelMappingContext.class)
//class BucketlistReviewControllerTest {
//
//    @Autowired
//    MockMvc mockMvc;
//
//    @Autowired
//    ObjectMapper objectMapper;
//
//    @MockBean
//    BucketlistReviewService bucketlistReviewService;
//
//    BucketlistReviewRequest bucketlistReviewRequest;
//
//
//    @Test
//    @DisplayName("")
//    void reviewList() {
//    }
//
//    @Test
//    @DisplayName("리뷰 작성")
//    void reviewCreate() throws Exception{
//        String result = "true";
//
//        when(bucketlistReviewService.create(any(), any(), any()))
//                .thenReturn(mock(String.class));
//
//        mockMvc.perform(post("/api/v1/posts/1/review")
////                .with(csrf())
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsBytes(bucketlistReviewRequest))
//                )
//
//                .andExpect(status().isOk())
//                .andDo(print());
//    }
//
//    @Test
//    @DisplayName("리뷰 수정")
//    void reviewUpdate() {
//    }
//
//    @Test
//    @DisplayName("리뷰 삭제")
//    void reviewDelete() {
//    }
//}