package com.team9.bucket_list.domain.dto.postFile;

import com.team9.bucket_list.domain.entity.PostFile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class DeleteFileResponse {

    private Long id;
    private String message;
    private String uploadFileName;
    private String awsS3FileName;

    public static DeleteFileResponse of(PostFile postFile) {
        return DeleteFileResponse.builder()
                .id(postFile.getId())
                .message("삭제 성공")
                .build();
    }

}
