package com.team9.bucket_list.service;

import com.team9.bucket_list.domain.dto.profile.ProfileRequest;
import com.team9.bucket_list.domain.dto.profile.ProfileResponse;
import com.team9.bucket_list.domain.entity.Member;
import com.team9.bucket_list.domain.entity.MemberReview;
import com.team9.bucket_list.execption.ApplicationException;
import com.team9.bucket_list.execption.ErrorCode;
import com.team9.bucket_list.repository.MemberRepository;
import com.team9.bucket_list.repository.MemberReviewRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
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

    @Transactional
    public String update(Long targetMemberId, String userName, ProfileRequest profileRequest, MultipartFile multipartFile) {
        Member targetMember = checkMemberId(targetMemberId);
        Member fromMember = checkMemberName(userName);

        if (!targetMemberId.equals(fromMember)) {
            throw new ApplicationException(ErrorCode.INVALID_PERMISSION);
        }

        targetMember.updateProfileImage(profileRequest.getImage());
        memberRepository.save(targetMember);
        return "true";
    }

}
