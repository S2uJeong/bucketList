package com.team9.bucket_list.domain.dto.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class MyFeedResponse {
    Page<PostReadResponse> createPosts;
    Page<PostReadResponse> applyPosts;
    Page<PostReadResponse> likePosts;
}
