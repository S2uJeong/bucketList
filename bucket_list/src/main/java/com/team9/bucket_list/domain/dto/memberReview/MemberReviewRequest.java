package com.team9.bucket_list.domain.dto.memberReview;

import com.team9.bucket_list.domain.entity.Member;
import com.team9.bucket_list.domain.entity.MemberReview;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MemberReviewRequest {

    private String message;
    private Integer score;

    public MemberReview toEntity(Member targetMember, Member fromMember) {
        return MemberReview.builder()
                .member(targetMember)
                .writerId(fromMember.getId())
                .content(message)
                .rate(score)
                .build();
    }

    public MemberReview update(MemberReviewRequest memberReviewRequest) {
        return MemberReview.builder()
                .content(memberReviewRequest.message)
                .rate(memberReviewRequest.score)
                .build();
    }
}
