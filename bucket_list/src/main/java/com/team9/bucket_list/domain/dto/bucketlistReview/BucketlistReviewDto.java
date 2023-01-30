package com.team9.bucket_list.domain.dto.bucketlistReview;

import com.team9.bucket_list.domain.entity.BucketlistReview;
import lombok.*;
import org.springframework.data.domain.Page;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BucketlistReviewDto {
    private Long id;
    private String content;
    private Long writerId;

    public static Page<BucketlistReviewDto> BucketlistReviewDto(Page<BucketlistReview> bucketlistReviews) {
        Page<BucketlistReviewDto> bucketlistReviewDtos = bucketlistReviews.map(bucketlistReview -> BucketlistReviewDto.builder()
                .id(bucketlistReview.getId())
                .content(bucketlistReview.getContent())
                .writerId(bucketlistReview.getWriterId())
                .build());

        return bucketlistReviewDtos;
    }
}
