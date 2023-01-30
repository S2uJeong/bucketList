package com.team9.bucket_list.domain.dto.bucketlistReview;

import com.team9.bucket_list.domain.entity.Bucketlist;
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

    public BucketlistReview toEntity(Member member, Bucketlist bucketlist) {
        return BucketlistReview.builder()
                .content(this.content)
                .writerId(member.getId())
                .bucketlist(bucketlist)
                .build();
    }
}
