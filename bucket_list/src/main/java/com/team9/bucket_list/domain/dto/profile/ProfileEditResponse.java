package com.team9.bucket_list.domain.dto.profile;

import com.team9.bucket_list.domain.entity.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileEditResponse {
    private String uploadFileName;
    private String awsS3FileName;

    public static ProfileEditResponse save(Profile profile) {
        return ProfileEditResponse.builder()
                .uploadFileName(profile.getUploadFileName())
                .awsS3FileName(profile.getAwsS3FileName())
                .build();
    }

}
