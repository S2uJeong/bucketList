package com.team9.bucket_list.service;

import com.team9.bucket_list.domain.dto.memberReview.MemberReviewRequest;
import com.team9.bucket_list.domain.dto.memberReview.MemberReviewResponse;
import com.team9.bucket_list.domain.entity.Member;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberReviewService {



    // 유효성 검사

    // 해당 member의 리뷰 리스트 확인

    // 해당 memeber의 특정 리뷰 확인

    // 해당 member의 리뷰 작성
    public MemberReviewResponse create(Long targerUserId, Long fromUserId, MemberReviewRequest memberReviewRequest) {
        return null;
    }
    // 해당 member의 리뷰 수정

    // 해당 member의 리뷰 삭제
}
