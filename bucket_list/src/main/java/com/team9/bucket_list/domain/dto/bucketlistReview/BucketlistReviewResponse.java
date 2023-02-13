package com.team9.bucket_list.domain.dto.bucketlistReview;

import com.team9.bucket_list.domain.entity.BucketlistReview;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BucketlistReviewResponse {
    private Long id;
    private String content;
    private Double rate;
    private Long writerId;
    private String writerName;
    private String createdAt;

    public static BucketlistReviewResponse response(BucketlistReview bucketlistReview, String writerName) {
        return BucketlistReviewResponse.builder()
                .id(bucketlistReview.getId())
                .content(bucketlistReview.getContent())
                .rate(bucketlistReview.getRate())
                .writerId(bucketlistReview.getWriterId())
                .writerName(writerName)
                .createdAt(bucketlistReview.getCreatedAt())
                .build();
    }
}
