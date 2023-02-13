package com.team9.bucket_list.service;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.team9.bucket_list.domain.dto.profile.ProfileReadResponse;
import com.team9.bucket_list.domain.dto.profile.ProfileEditResponse;
import com.team9.bucket_list.domain.entity.*;
import com.team9.bucket_list.execption.ApplicationException;
import com.team9.bucket_list.execption.ErrorCode;
import com.team9.bucket_list.repository.MemberRepository;
import com.team9.bucket_list.repository.ProfileRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileService {

    private final AmazonS3Client amazonS3Client;
    private final MemberRepository memberRepository;
    private final ProfileRepository profileRepository;

    private final MemberReviewService memberReviewService;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // ========== 유효성 검사 ==========
    public Member checkMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.USERNAME_NOT_FOUNDED));
    }

    public void checkAuthority(Long memberId, Long profileMemberId) {
        Member loginMember = checkMember(memberId);
        Member profileMember = checkMember(profileMemberId);
        if(loginMember.getUserName().equals(profileMember.getUserName()))
            throw new ApplicationException(ErrorCode.INVALID_PERMISSION);
    }

    public Profile checkProfile(Long profileId) {
        return profileRepository.findById(profileId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.PROFILE_NOT_FOUND));
    }


    // 상세조회
    public ProfileReadResponse read(Long memberId) {
        // 유효성 검사
        Member member = checkMember(memberId); // 프로필을 가진 대상이 존재 한 지
        Optional<Profile> savedProfile = profileRepository.findByMember_Id(memberId);
        if (savedProfile.isPresent()) { // 이 member가 기존에 프로필이 있다면 가져온다.
            log.info("🆘" + profileRepository.findByMember_Id(memberId).get().getId());
            return ProfileReadResponse.detailOf((profileRepository.findByMember_Id(memberId)).get());
        } else { // 기존 프로필이 없다면 기본프로필로 가져온다.
            log.info("🆘" + "프로필 없어요");
            Profile profile = Profile.save("기본사진.png", "https://bucketlist-post-image-bucket.s3.ap-northeast-2.amazonaws.com/%EA%B8%B0%EB%B3%B8%EC%82%AC%EC%A7%84.png", memberReviewService.calaulateScore(memberId), member);
            profileRepository.save(profile);
            return ProfileReadResponse.detailOf(profile);
        }
    }

    @Transactional
    public ProfileEditResponse update(Long memberId, MultipartFile multipartFile) {

        if (multipartFile.isEmpty()) {  // 요청으로 들어온 file이 존재한 지 확인
            throw new ApplicationException(ErrorCode.FILE_NOT_EXISTS);
        }
        Member member = checkMember(memberId); // 프로필을 가진 대상이 존재한지 확인
        Profile profile = profileRepository.findByMember_Id(memberId).get();
/**
         * Authentication 추가해서 유효성 검사 추가해야함
         * 1. 로그인 유저 존재하는지
         * 2. 로그인 한 유저와 수정하려는 프로필의 유저가 일치한지. ( 프로필:유저 = 1:1 - 매핑완)
         */

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());
        objectMetadata.setContentLength(multipartFile.getSize());

        // 사용자가 올린 파일 이름
        String uploadFileName = multipartFile.getOriginalFilename();
        // file 형식이 잘못된 경우를 확인
        int index;
        try {
            index = uploadFileName.lastIndexOf(".");
        } catch (StringIndexOutOfBoundsException e) {
            throw new ApplicationException(ErrorCode.WRONG_FILE_FORMAT);
        }

        String ext = uploadFileName.substring(index + 1);

        String awsS3FileName = UUID.randomUUID() + "." + ext;

        String key = "profileImage/" + awsS3FileName;

        try (InputStream inputStream = multipartFile.getInputStream()) {
            amazonS3Client.putObject(new PutObjectRequest(bucket, key, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            throw new ApplicationException(ErrorCode.FILE_UPLOAD_ERROR);
        }
        // 이미지 조회 가능한 url 주소
        String fileUrl = amazonS3Client.getUrl(bucket, key).toString();

        // 프로필 db에 저장하기
        Profile savedProfile = Profile.updateImage(uploadFileName, key, profile);
        profileRepository.save(savedProfile);
        log.info("프로필 등록 완료");

        return ProfileEditResponse.save(savedProfile);
    }
}
