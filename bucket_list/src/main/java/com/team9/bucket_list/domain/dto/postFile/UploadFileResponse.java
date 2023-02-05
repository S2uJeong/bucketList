package com.team9.bucket_list.domain.dto.postFile;

import com.team9.bucket_list.domain.entity.PostFile;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class UploadFileResponse {

    private String message;
    private String uploadFileName;
    private String awsS3FileName;

    public static UploadFileResponse of(String uploadFileNam, String awsS3FileName ) {
        return UploadFileResponse.builder()
                .uploadFileName(uploadFileNam)
                .awsS3FileName(awsS3FileName)
                .message("파일 첨부 완료")
                .build();
    }
}