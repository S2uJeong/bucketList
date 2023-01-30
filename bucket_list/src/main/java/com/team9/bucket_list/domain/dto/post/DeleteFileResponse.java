package com.team9.bucket_list.domain.dto.post;

import com.team9.bucket_list.domain.entity.File;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class DeleteFileResponse {

    private Long deletedFile;
    private String message;

    public static DeleteFileResponse of(File file) {
        return DeleteFileResponse.builder()
                .deletedFile(file.getId())
                .message("삭제 성공")
                .build();
    }

}
