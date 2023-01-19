package com.team9.bucket_list.controller.rest;

import com.team9.bucket_list.domain.dto.memberReview.MemberReviewRequest;
import com.team9.bucket_list.domain.dto.memberReview.MemberReviewResponse;
import com.team9.bucket_list.service.MemberReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/rating")
@RequiredArgsConstructor
public class MemeberReviewController {

    private final MemberReviewService memberReviewService;

    @PostMapping("/{targetUserId}/{fromUserId}")
    public MemberReviewResponse create(@PathVariable Long targetUserId, @PathVariable Long fromUserId, MemberReviewRequest memberReviewRequest) {
        memberReviewService.create(targetUserId, fromUserId, memberReviewRequest);
    }
}
