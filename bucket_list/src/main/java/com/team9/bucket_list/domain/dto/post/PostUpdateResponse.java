package com.team9.bucket_list.domain.dto.post;
import com.team9.bucket_list.domain.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class PostUpdateResponse {
    private String message;
    private Long postId;

    public static PostUpdateResponse of(Post post) {
        return PostUpdateResponse.builder()
                .message("포스트 수정 완료")
                .postId(post.getId())
                .build();
    }

}
