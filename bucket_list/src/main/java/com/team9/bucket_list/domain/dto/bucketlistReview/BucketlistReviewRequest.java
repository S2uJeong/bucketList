package com.team9.bucket_list.domain.dto.bucketlistReview;

import com.team9.bucket_list.domain.entity.Bucketlist;
import com.team9.bucket_list.domain.entity.BucketlistReview;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BucketlistReviewRequest {
    private String content;
//    private int number; 리뷰가 몇개 달려있는지? 아니면 몇번째?

    public BucketlistReview toEntity(Bucketlist bucketlist) {
        return BucketlistReview.builder()
                .content(this.content)
                .writerId(bucketlist.getId())
                .bucketlist(bucketlist)
                .build();
    }
}
