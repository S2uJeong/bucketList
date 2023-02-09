package com.team9.bucket_list.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.team9.bucket_list.domain.dto.postFile.DeleteFileResponse;
import com.team9.bucket_list.domain.dto.postFile.UploadFileResponse;
import com.team9.bucket_list.domain.entity.Post;
import com.team9.bucket_list.domain.entity.PostFile;
import com.team9.bucket_list.execption.ApplicationException;
import com.team9.bucket_list.execption.ErrorCode;
import com.team9.bucket_list.repository.PostFileRepository;
import com.team9.bucket_list.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;


@Service
@Slf4j
@RequiredArgsConstructor
public class ProfileFileService {

    private final AmazonS3Client amazonS3Client;
    private final PostFileRepository postFileRepository;
    private final PostRepository postRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // 업로드
    public UploadFileResponse upload(Long postId, MultipartFile multipartFile) throws IOException {
        // 빈 파일인지 확인
        if (multipartFile.isEmpty()) {
            throw new ApplicationException(ErrorCode.FILE_NOT_EXISTS);
        }
        // 파일 업로드 대상인 post 존재유무 확인
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.POST_NOT_FOUND));

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());
        objectMetadata.setContentLength(multipartFile.getSize());

        // 업로드한 파일 이름
        String uploadFileName = multipartFile.getOriginalFilename();

        // file 형식이 잘못된 경우를 확인
        int index;
        try {
            index = uploadFileName.lastIndexOf(".");
        } catch (StringIndexOutOfBoundsException e) {
            throw new ApplicationException(ErrorCode.WRONG_FILE_FORMAT);
        }

        String ext = uploadFileName.substring(index + 1);

        // 저장될 파일 이름 앞에 랜덤 값을 붙여 중복 방지
        String awsS3FileName = UUID.randomUUID() + "." + ext;

        // 저장할 디렉토리 경로 + 파일 이름
        String key = "postImage/" + awsS3FileName;

        try (InputStream inputStream = multipartFile.getInputStream()) {
            amazonS3Client.putObject(new PutObjectRequest(bucket, key, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            throw new ApplicationException(ErrorCode.FILE_UPLOAD_ERROR);
        }

        // db에 저장하기
        String fileUrl = amazonS3Client.getUrl(bucket, key).toString();
        log.info("🔵 amazonS3Client.getUrl = {} ", fileUrl );
        // PostFile postFile = PostFile.save(uploadFileName, fileUrl, post);
        PostFile postFile = PostFile.save(uploadFileName, key, post);
        postFileRepository.save(postFile);
        log.info("🔵 파일 등록 완료 ");
        return UploadFileResponse.of(uploadFileName,awsS3FileName);

    }


    // 업로드한 파일 삭제
    public DeleteFileResponse delete(Long postId, Long postFileId) {
        // 유효성검사 - 삭제 하려는 file의 존재여부
        PostFile postFile = postFileRepository.findById(postFileId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.FILE_NOT_EXISTS));
        // 유효성검사 - 파일 업로드 대상인 post 존재유무 확인
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.POST_NOT_FOUND));

        try {
            // S3 업로드 파일 삭제
            amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, postFile.getAwsS3FileName()));
            // 해당 업로드 파일 테이블에서도 같이 삭제
            postFileRepository.delete(postFile);
            log.info("🔵 파일 삭제 성공");
        } catch (AmazonServiceException e) {
            e.printStackTrace();
        } catch (SdkClientException e) {
            e.printStackTrace();
        }
        return DeleteFileResponse.of(postFile);
    }


}
