package com.team9.bucket_list.domain.dto.bucketlistReview;

import com.team9.bucket_list.domain.entity.BucketlistReview;
import com.team9.bucket_list.domain.entity.Member;
import com.team9.bucket_list.domain.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BucketlistReviewRequest {

    private String content;
    private Integer rate;
    private Long targetPostId;

    public BucketlistReview toEntity(Post post, Member member) {
        return BucketlistReview.builder()
                .post(post)
                .writerId(member.getId())
                .content(this.content)
                .rate(this.rate)
                .build();
    }
}
