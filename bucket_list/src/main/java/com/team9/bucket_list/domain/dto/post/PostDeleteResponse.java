package com.team9.bucket_list.domain.dto.post;
import com.team9.bucket_list.domain.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class PostDeleteResponse {
    private Long postId;
    private String message;

    public static PostDeleteResponse of(Post post) {
        return PostDeleteResponse.builder()
                .message("포스트 삭제완료")
                .postId(post.getId())
                .build();
    }
}
