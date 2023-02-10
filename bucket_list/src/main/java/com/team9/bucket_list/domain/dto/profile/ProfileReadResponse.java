package com.team9.bucket_list.domain.dto.profile;

import com.team9.bucket_list.domain.dto.post.PostReadResponse;
import com.team9.bucket_list.domain.entity.*;
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
public class ProfileReadResponse {
    private String memberName;

    private String email;
    private double avgRate;
    private String uploadFileName; // 사용자가 설정한 파일 이름
    private String awsS3FileName; // DB에 저장된 파일의 URL


    public static ProfileReadResponse detailOf(Profile profile) {
        return ProfileReadResponse.builder()
                .memberName(profile.getMember().getUserName())
                .email(profile.getMember().getEmail())
                .avgRate(profile.getAvgRate())
                .uploadFileName(profile.getUploadFileName())
                .awsS3FileName(profile.getAwsS3FileName())
                .build();
    }

}
