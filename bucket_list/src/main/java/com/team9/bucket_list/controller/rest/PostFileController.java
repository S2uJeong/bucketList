package com.team9.bucket_list.controller.rest;
import com.team9.bucket_list.domain.Response;
import com.team9.bucket_list.domain.dto.postFile.DeleteFileResponse;
import com.team9.bucket_list.domain.dto.postFile.UploadFileResponse;
import com.team9.bucket_list.service.PostFileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("post")
@Tag(name = "게시글 사진파일", description = "게시글 작성 시 첨부한 사진 파일을 서버에 업로드 및 삭제 합니다.")
public class PostFileController {

    private final PostFileService postFileService;

    // S3에 파일 업로드
    @PostMapping(value = "/{postId}/files/upload",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "파일 업로드", description = "해당 게시글에 첨부된 파일을 S3 버킷에 업로드하고, 서버 DB에 해당 파일 S3객체 URL을 저장 합니다.")
    public Response<UploadFileResponse> upload(@Parameter(name = "postId", description = "게시글 id") @PathVariable("postId") Long postId,
                                               @RequestPart(value="file",required = false) MultipartFile file) throws IOException {
        UploadFileResponse response = postFileService.upload(postId, file);
        return Response.success(response);
    }

    // file 삭제
    @DeleteMapping("/{postId}/files/{postFileId}")
    @Operation(summary = "파일 삭제", description = "해당 게시글에 첨부된 파일을 S3 버킷에서 삭제 하고, 서버 DB에 해당 파일 S3객체 URL을 삭제 합니다.")
    public Response<DeleteFileResponse> delete(@Parameter(name = "postId", description = "게시글 id") @PathVariable("postId") Long postId,
                                               @Parameter(name = "postFileId", description = "파일 id") @PathVariable("postFileId") Long postFileId) {
        DeleteFileResponse response = postFileService.delete(postId, postFileId);
        return Response.success(response);
    }



}
