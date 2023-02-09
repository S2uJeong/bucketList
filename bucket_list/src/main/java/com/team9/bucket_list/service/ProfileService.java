package com.team9.bucket_list.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.team9.bucket_list.domain.dto.profile.ProfileResponse;
import com.team9.bucket_list.domain.entity.Member;
import com.team9.bucket_list.domain.entity.MemberReview;
import com.team9.bucket_list.domain.entity.Profile;
import com.team9.bucket_list.execption.ApplicationException;
import com.team9.bucket_list.execption.ErrorCode;
import com.team9.bucket_list.repository.MemberRepository;
import com.team9.bucket_list.repository.MemberReviewRepository;
import com.team9.bucket_list.repository.comment.ProfileRepository;
import jakarta.persistence.criteria.Path;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileService {

    private final AmazonS3Client amazonS3Client;
    private final MemberRepository memberRepository;
    private final MemberReviewRepository memberReviewRepository;
    private final ProfileRepository profileRepository;

    // ========== 유효성 검사 ==========
    public Member checkMemberId(Long targetMemberId) {
        Member member = memberRepository.findById(targetMemberId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.USERNAME_NOT_FOUNDED));
        return member;
    }

    public Member checkMemberName(String userName) {
        Member member = memberRepository.findByUserName(userName)
                .orElseThrow(() -> new ApplicationException(ErrorCode.USERNAME_NOT_FOUNDED));
        return member;
    }


    // ========== detail, update ==========
    public List<ProfileResponse> detail(Long targetMemberId) {
        Member member = checkMemberId(targetMemberId);
        log.info("service의 detail입니다.");

        // 평점 평균
        // 람다로 표현 할 방법은 없을까?
        double avg = 0;
        List<MemberReview> memberReviewList = memberReviewRepository.findAllByMember_Id(targetMemberId);
        for (MemberReview m : memberReviewList) {
            avg += m.getRate();
        }
        avg = (avg / memberReviewList.size());
        avg = Math.round(avg * 100) / 100.0;

        member.rateUpdate(avg);

        List<Member> memberProfile = new ArrayList<>();

        memberProfile.add(member);

        List<ProfileResponse> memberDetailList = ProfileResponse.response(memberProfile, avg);

        return memberDetailList;
    }


    @Value("${cloud.aws.s3.bucket}")
    private String uploadFolder;

    @Transactional
    public ProfileResponse update(Long memberId, String userName, MultipartFile multipartFile) {

        if (multipartFile.isEmpty()) {
            throw new ApplicationException(ErrorCode.FILE_NOT_EXISTS);
        }

        Member member = checkMemberId(memberId);

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());
        objectMetadata.setContentLength(multipartFile.getSize());

        // 사용자가 올린 파일 이름
        String uploadFileName = multipartFile.getOriginalFilename();

        int index;

        try {
            index = uploadFileName.lastIndexOf(".");
        } catch (StringIndexOutOfBoundsException e) {
            throw new ApplicationException(ErrorCode.WRONG_FILE_FORMAT);
        }

        if (!member.getUserName().equals(userName)) {
            new ApplicationException(ErrorCode.INVALID_PERMISSION);
        }

        String ext = uploadFileName.substring(index + 1);

        String awsS3FileName = UUID.randomUUID() + "." + ext;

        String key = "profileImage/" + awsS3FileName;

        try (InputStream inputStream = multipartFile.getInputStream()) {
            amazonS3Client.putObject(new PutObjectRequest(uploadFolder, key, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            throw new ApplicationException(ErrorCode.FILE_UPLOAD_ERROR);
        }

        // db에 저장하기
        String fileUrl = amazonS3Client.getUrl(uploadFolder, key).toString();
        log.info("🔵 amazonS3Client.getUrl = {} ", fileUrl );
        // PostFile postFile = PostFile.save(uploadFileName, fileUrl, post);
        Profile profile = Profile.save(uploadFileName, key, member);
        profileRepository.save(profile);
        log.info("🔵 파일 등록 완료 ");

        return ProfileResponse.updateProfileImage(uploadFileName, awsS3FileName);
    }

//    // .yml에 작성된 file : path : 경로
//    @Value("${file.path}")
//    private String uploadFolder;
//
//    @Transactional
//    public String update(Long memberId, String userName, MultipartFile multipartFile) {
//
//        Member member = checkMemberId(memberId);
//
//        if (!member.getUserName().equals(userName)) {
//            new ApplicationException(ErrorCode.INVALID_PERMISSION);
//        }
//
//        UUID uuid = UUID.randomUUID();
//
//        String imageFileName = uuid + "_" + multipartFile.getOriginalFilename(); // 이름.jpg 형식
//        log.info("image file name : " + imageFileName);
//
//        Path imageFilePath = (Path) Paths.get(uploadFolder + imageFileName);
//
//        try {
//            // 줄 구분 기호로 끝나는 각 줄을 사용하여 파일에 순서대로 기록(문자는 지정된 문자 집합을 사용하여 바이트로 인코딩)
//            Files.write((java.nio.file.Path) imageFilePath, multipartFile.getBytes());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        member.updateProfileImage(imageFileName);
//
//        return "True";
//
//    }

}
