package com.team9.bucket_list.domain.dto.memberReview;

import com.team9.bucket_list.domain.entity.MemberReview;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class MemberReviewResponse {

    private Long id;
    private String content;
    private Integer score;
    private Long writerId;

    public static MemberReviewResponse response(MemberReview memberReview) {
        return MemberReviewResponse.builder()
                .id(memberReview.getId())
                .content(memberReview.getContent())
                .score(memberReview.getRate())
                .writerId(memberReview.getWriterId())
                .build();
    }
}
