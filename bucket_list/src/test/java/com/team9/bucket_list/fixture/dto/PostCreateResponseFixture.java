package com.team9.bucket_list.fixture.dto;

import com.team9.bucket_list.domain.dto.post.PostCreateResponse;
import com.team9.bucket_list.domain.entity.Post;

public class PostCreateResponseFixture {

    public static PostCreateResponse get(Post post){
        return PostCreateResponse.builder()
                .postId(1L)
                .message("post 등록완료")
                .build();
    }
}
