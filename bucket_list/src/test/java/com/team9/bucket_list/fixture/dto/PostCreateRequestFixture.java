package com.team9.bucket_list.fixture.dto;

import com.team9.bucket_list.domain.dto.post.PostCreateRequest;

public class PostCreateRequestFixture {
    public static PostCreateRequest get(){
        return PostCreateRequest.builder()
                .title("title")
                .untilRecruit("02/15/2023")
                .eventStart("02/21/2023")
                .eventEnd("02/25/2023")
                .category("TRAVEL")
                .entrantNum(5)
                .cost(10000)
                .location("서울 시청")
                .content("content")
                .build();
    }

}
