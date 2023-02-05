package com.team9.bucket_list.controller.rest;
import com.team9.bucket_list.domain.Response;
import com.team9.bucket_list.domain.dto.postFile.DeleteFileResponse;
import com.team9.bucket_list.domain.dto.postFile.UploadFileResponse;
import com.team9.bucket_list.service.PostFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("post")
public class PostFileController {

    private final PostFileService postFileService;

    // S3에 파일 업로드
    @PostMapping(value = "/{postId}/files/upload",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE}
    )
    public Response<UploadFileResponse> upload(@PathVariable("postId") Long postId,
                                               @RequestPart(value="file",required = false) MultipartFile file) throws IOException {
        UploadFileResponse response = postFileService.upload(postId, file);
        return Response.success(response);
    }

    // file 삭제
    @DeleteMapping("/{postId}/files/{postFileId}")
    public Response<DeleteFileResponse> delete(@PathVariable("postId") Long postId,
                                               @PathVariable("postFileId") Long postFileId) {
        DeleteFileResponse response = postFileService.delete(postId, postFileId);
        return Response.success(response);
    }




}
