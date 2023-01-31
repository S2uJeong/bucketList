package com.team9.bucket_list.service;

import com.team9.bucket_list.domain.dto.profile.ProfileResponse;
import com.team9.bucket_list.domain.entity.Member;
import com.team9.bucket_list.domain.entity.MemberReview;
import com.team9.bucket_list.execption.ApplicationException;
import com.team9.bucket_list.execption.ErrorCode;
import com.team9.bucket_list.repository.MemberRepository;
import com.team9.bucket_list.repository.MemberReviewRepository;
import jakarta.persistence.criteria.Path;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileService {

    private MemberRepository memberRepository;
    private MemberReviewRepository memberReviewRepository;

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

        // 평점 평균
        // 람다로 표현 할 방법은 없을까?
        double avg = 0;
        List<MemberReview> memberReviewList = memberReviewRepository.findAllByMember_Id(targetMemberId);
        for ( MemberReview m : memberReviewList) {
            avg += m.getRate();
        }
        avg = (avg / memberReviewList.size());
        avg = Math.round(avg*100)/100.0;

        List<Member> memberProfile = new ArrayList<>();

        memberProfile.add(member);

        List<ProfileResponse> memberDetailList = ProfileResponse.response(memberProfile, avg);

        return memberDetailList;
    }


    // .yml에 작성된 file : path : 경로
    @Value("${file.path}")
    private String uploadFolder;

    public String update(Long memberId, String userName, MultipartFile multipartFile) {

        Member member = checkMemberId(memberId);

        if (!member.getUserName().equals(userName)) {
            new ApplicationException(ErrorCode.INVALID_PERMISSION);
        }

        UUID uuid = UUID.randomUUID();

        String imageFileName = uuid + "_" + multipartFile.getOriginalFilename(); // 이름.jpg 형식
        log.info("image file name : " + imageFileName);

        Path imageFilePath = (Path) Paths.get(uploadFolder + imageFileName);

        try {
            // 줄 구분 기호로 끝나는 각 줄을 사용하여 파일에 순서대로 기록(문자는 지정된 문자 집합을 사용하여 바이트로 인코딩)
            Files.write((java.nio.file.Path) imageFilePath, multipartFile.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }

        member.updateProfileImage(imageFileName);

        return "True";

    }

}
