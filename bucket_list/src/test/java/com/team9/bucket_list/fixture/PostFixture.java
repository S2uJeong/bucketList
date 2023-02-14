package com.team9.bucket_list.fixture;

import com.team9.bucket_list.domain.entity.Post;

public class PostFixture {
    public static Post get() {
        return Post.builder()
                .id(1L)
                .title("test")
                .content("test")
                .location("testLocation")
                .cost(10000)
                .untilRecruit("test until recruit")
                .entrantNum(1)
                .eventStart("03/01/2023")
                .eventEnd("03/01/2023")
                .category("TRAVEL")
                .member(MemberFixture.get("email","passwrd","name"))
                .build();
    }
}
