package com.team9.bucket_list.domain.dto.profile;

import com.team9.bucket_list.domain.entity.Alarm;
import com.team9.bucket_list.domain.entity.Member;
import com.team9.bucket_list.domain.entity.MemberReview;
import com.team9.bucket_list.domain.entity.Profile;
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

    private String uploadFileName;
    private String awsS3FileName;

    private Long memberId;

    private double rate;

    private List<MemberReview> memberReviewList;

    // member1.getPostImageUrl() 이 부분 수정 및 엔티티도 수정 해야할듯
    public static List<ProfileResponse> response(List<Member> member, double rate) {
        List<ProfileResponse> result = (List<ProfileResponse>) member.stream().map(member1 -> ProfileResponse.builder()
                .awsS3FileName(member1.getPostImageUrl())
                .memberId(member1.getId())
                .rate(rate)
                .build());
        return result;
    }


    public static ProfileResponse updateProfileImage(String uploadFileName, String awsS3FileName) {
        return ProfileResponse.builder()
                .uploadFileName(uploadFileName)
                .awsS3FileName(awsS3FileName)
                .build();
    }
}
