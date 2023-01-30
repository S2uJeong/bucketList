package com.team9.bucket_list.service;

import com.team9.bucket_list.domain.dto.memberReview.MemberReviewRequest;
import com.team9.bucket_list.domain.entity.Alarm;
import com.team9.bucket_list.domain.entity.Member;
import com.team9.bucket_list.domain.entity.MemberReview;
import com.team9.bucket_list.execption.ApplicationException;
import com.team9.bucket_list.execption.ErrorCode;
import com.team9.bucket_list.repository.AlarmRepository;
import com.team9.bucket_list.repository.MemberRepository;
import com.team9.bucket_list.repository.MemberReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberReviewService {

    private final MemberRepository memberRepository;
    private final MemberReviewRepository memberReviewRepository;
    private final AlarmRepository alarmRepository;

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

    public Page<MemberReview> list (Long targetUserId, Pageable pageable) {
        Member member = checkMemberId(targetUserId);
        return memberReviewRepository.findAllByMember(member, pageable);
    }

//    public void score (Long targetUserId) {
//        MemberReview memberReview = memberReviewRepository.findByUserId(targetUserId)
//                .orElseThrow(() -> new ApplicationException(ErrorCode.REVIEW_NOT_FOUND));
//
//        double avg = 0;
//        List<MemberReview> memberReviewList = memberReviewRepository.findAllByUserId(targetUserId);
//        for ( MemberReview m : memberReviewList) {
//            avg += m.getRate();
//        }
//        avg = (avg / memberReviewList.size());
//        avg = Math.round(avg*100)/100.0;
//
////        memberReviewRepository.findByUserId(targetUserId);
//
//    }


    public String create(Long targetUserId, String userName, MemberReviewRequest memberReviewRequest) {

        Member targetMember = checkMemberId(targetUserId);
        Member fromMember = checkMemberName(userName);

        if(!targetMember.getMemberBucketlistList().contains(fromMember)) {
            throw new ApplicationException(ErrorCode.INVALID_PERMISSION);
        }

        MemberReview memberReview = memberReviewRepository.save(memberReviewRequest.toEntity(targetMember, fromMember));

        alarmRepository.save(Alarm.of(targetMember, targetMember.getUserName()+"에 대한 리뷰가 작성 되었습니다."));


        return "true";
    }

    public String update(Long targetUserId, Long reviewId, String userName, MemberReviewRequest memberReviewRequest) {

        Member targetMember = checkMemberId(targetUserId);
        Member fromMember = checkMemberName(userName);
        MemberReview memberReview = checkMemberReview(reviewId);

        if (memberReview.getWriterId() != fromMember.getId()) {
            throw new ApplicationException(ErrorCode.INVALID_PERMISSION);
        }

        memberReviewRepository.save(memberReviewRequest.update(memberReviewRequest));

        alarmRepository.save(Alarm.of(targetMember, targetMember.getUserName()+"에 대한 리뷰가 수정 되었습니다."));

        return "true";
    }

    public String delete(Long targetUserId, Long reviewId, String userName) {

        Member targetMember = checkMemberId(targetUserId);
        Member fromMember = checkMemberName(userName);
        MemberReview memberReview = checkMemberReview(reviewId);

        if (memberReview.getWriterId() != fromMember.getId()) {
            throw new ApplicationException(ErrorCode.INVALID_PERMISSION);
        }

        alarmRepository.save(Alarm.of(targetMember, targetMember.getUserName()+"에 대한 리뷰가 삭제 되었습니다."));

        memberReviewRepository.deleteById(memberReview.getId());

        return "true";
    }


}
