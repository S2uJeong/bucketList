package com.team9.bucket_list.domain.dto.memberReview;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MemberReviewResponse {
    private String content;
    private Integer score;
}
