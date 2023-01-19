package com.team9.bucket_list.domain.dto.post;
import com.team9.bucket_list.domain.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class PostCreateResponse {

    private Long postId;
    private String message;

    public static PostCreateResponse of(Post post) {
        return PostCreateResponse.builder()
                .message("post 등록완료")
                .postId(post.getId())
                .build();
    }

}
