package com.team9.bucket_list.fixture;


import com.team9.bucket_list.domain.entity.Post;
import com.team9.bucket_list.domain.entity.Member;
import com.team9.bucket_list.domain.enumerate.PostStatus;
import lombok.Builder;

// 임의 Entity Response 값
@Builder
public class PostFixture {
    public static Post get() {
        return Post.builder()
                .id(1L)
                .title("test")
                .content("test")
                .location("testLocation")
                .cost(10000)
                .untilRecruit("02/20/2023")
                .entrantNum(1)
                .eventStart("03/01/2023")
                .eventEnd("03/01/2023")
                .category("TRAVEL")
                .status(PostStatus.JOIN)
                .member(MemberFixture.get("email","passwrd","name"))
                .build();
    }

    public static Post get(Member member) {
        return Post.builder()
                .id(1L)
                .title("test")
                .content("test")
                .location("testLocation")
                .cost(10000)
                .untilRecruit("02/20/2023")
                .entrantNum(1)
                .eventStart("03/01/2023")
                .eventEnd("03/01/2023")
                .category("TRAVEL")
                .status(PostStatus.JOIN)
                .member(MemberFixture.get("email","passwrd","name"))
                .build();
    }

}
