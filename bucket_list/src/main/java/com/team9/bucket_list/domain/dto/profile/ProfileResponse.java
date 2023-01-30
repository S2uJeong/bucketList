package com.team9.bucket_list.domain.dto.profile;

import com.team9.bucket_list.domain.entity.Alarm;
import com.team9.bucket_list.domain.entity.Member;
import com.team9.bucket_list.domain.entity.MemberReview;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileResponse {
//    알람을 어떤 방식으로 해야할지 모르겠다.
//    private List<Alarm> alarm;

    private String image;
    private Long memberId;

    private double rate;

    private List<MemberReview> memberReviewList;

    public static List<ProfileResponse> response(List<Member> member, double rate) {
        List<ProfileResponse> result = (List<ProfileResponse>) member.stream().map(member1 -> ProfileResponse.builder()
                .image(member1.getImage())
                .memberId(member1.getId())
                .rate(rate)
                .build());
        return result;
    }
}
