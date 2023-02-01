package com.team9.bucket_list.domain.dto.post;

import com.team9.bucket_list.domain.entity.File;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UploadFileResponse {

    private String uploadFileName;
    private String message;

    public static UploadFileResponse of(File file) {
        return UploadFileResponse.builder()
                .uploadFileName(file.getUploadFileName())
                .message("파일 첨부 완료")
                .build();
    }
}