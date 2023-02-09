package com.team9.bucket_list.domain.dto.profile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class MyPageEditRequest {
    private MultipartFile multipartFile;
}
