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
    private Double rate;
    private Long writerId;
    private String writerName;
    private String createdAt;

    public static MemberReviewResponse response(MemberReview memberReview, String writerName) {
        return MemberReviewResponse.builder()
                .id(memberReview.getId())
                .content(memberReview.getContent())
                .rate(memberReview.getRate())
                .writerId(memberReview.getWriterId())
                .writerName(writerName)
                .createdAt(memberReview.getCreatedAt())
                .build();
    }
}
