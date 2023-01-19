package com.team9.bucket_list.domain.dto.memberReview;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MemberReviewRequest {
    // 작성자 id
    private Long userId;

    private String content;
    private Integer score;
}
