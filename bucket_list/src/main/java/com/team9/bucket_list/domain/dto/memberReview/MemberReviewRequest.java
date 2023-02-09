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

    private String content;
    private Integer rate;
    private String targetMemberName;

    public MemberReview toEntity(Member targetMember, Member fromMember) {
        return MemberReview.builder()
                .member(targetMember)
                .writerId(fromMember.getId())
                .content(this.content)
                .rate(this.rate)
                .build();
    }
}
