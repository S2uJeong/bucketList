package com.team9.bucket_list.service;

import com.team9.bucket_list.domain.dto.memberReview.MemberReviewRequest;
import com.team9.bucket_list.domain.dto.memberReview.MemberReviewResponse;
import com.team9.bucket_list.domain.entity.Member;
import com.team9.bucket_list.domain.entity.MemberReview;
import com.team9.bucket_list.execption.ApplicationException;
import com.team9.bucket_list.execption.ErrorCode;
import com.team9.bucket_list.repository.AlarmRepository;
import com.team9.bucket_list.repository.MemberRepository;
import com.team9.bucket_list.repository.MemberReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberReviewService {

    private final MemberRepository memberRepository;
    private final MemberReviewRepository memberReviewRepository;

    public Member checkMemberId(Long targetUserId) {
        return memberRepository.findById(targetUserId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.USERNAME_NOT_FOUNDED));
    }

    public Member checkMemberName(String fromUserName) {
        return memberRepository.findByUserName(fromUserName)
                .orElseThrow(() -> new ApplicationException(ErrorCode.USERNAME_NOT_FOUNDED));
    }

    public MemberReview checkMemberReview(Long reviewId) {
        return memberReviewRepository.findById(reviewId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.REVIEW_NOT_FOUND));
    }

    public Page<MemberReviewResponse> list (Long targetUserId, Pageable pageable) {
        Member member = checkMemberId(targetUserId);
        Page<MemberReviewResponse> memberReviews = memberReviewRepository.findAllByMember(member, pageable)
                .map(memberReview -> MemberReviewResponse.response(memberReview, memberReview.getMember().getUserName()));
        return memberReviews;
    }

    public String create(Long memberId, MemberReviewRequest memberReviewRequest) {

        Member targetMember = checkMemberName(memberReviewRequest.getTargetMemberName());
        Member fromMember = checkMemberId(memberId);

        Optional<MemberReview> mem = memberReviewRepository.findByMember_UserNameAndWriterId(memberReviewRequest.getTargetMemberName(), memberId);
        if(mem.isPresent()){
//            throw new ApplicationException(ErrorCode.DUPLICATED_REVIEW);
            return "duplicated";
        }

        memberReviewRepository.save(memberReviewRequest.toEntity(targetMember, fromMember));

        return "true";
    }

    public double calaulateScore (Long memberId) {
/*        MemberReview memberReview = memberReviewRepository.findByMember_Id(memberId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.REVIEW_NOT_FOUND));

        double avg = 0;
        List<MemberReview> memberReviewList = memberReviewRepository.findAllByMember_Id(memberId);
        for ( MemberReview m : memberReviewList) {
            avg += m.getRate();
        }
        avg = (avg / memberReviewList.size());
        return Math.round(avg*10)/10.0;*/

        return memberReviewRepository.averageByMemberId(memberId);
    }

}
