package com.team9.bucket_list.controller.rest;

import com.team9.bucket_list.domain.Response;
import com.team9.bucket_list.domain.dto.memberReview.MemberReviewRequest;
import com.team9.bucket_list.domain.dto.memberReview.MemberReviewResponse;
import com.team9.bucket_list.domain.entity.Member;
import com.team9.bucket_list.service.MemberReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/v1/rating")
@RequiredArgsConstructor
public class MemeberReviewController {

    private final MemberReviewService memberReviewService;

    @GetMapping("/{targerUserId}")
    public Page<MemberReviewResponse> list(@PathVariable Long targerUserId, @PageableDefault(sort = "createdAt", size = 20, direction = Sort.Direction.DESC) Pageable pageable) {
        Page<MemberReviewResponse> memberReviewResponses = memberReviewService.list(targerUserId, pageable)
                .map(memberReview -> MemberReviewResponse.response(memberReview));
        return memberReviewResponses;
    }

    @PostMapping("/{userId}")
    public Response<String> createRating(@PathVariable(name = "userId") Long targetUserId, Authentication authentication, MemberReviewRequest memberReviewRequest) {
        String userName = authentication.getName();
        String result = memberReviewService.create(targetUserId, userName, memberReviewRequest);
        return Response.success(result);
    }

    @PutMapping("/{userId}/{ratingId}")
    public Response<String> updateRating(@PathVariable(name = "userId") Long targetUserId, @PathVariable(name = "ratingId") Long reviewId, Authentication authentication, MemberReviewRequest memberReviewRequest) {
        String userName = authentication.getName();
        String result = memberReviewService.update(targetUserId, reviewId, userName, memberReviewRequest);
        return Response.success(result);
    }

    @DeleteMapping("/{userId}/{ratingId}")
    public Response<String> deleteRating(@PathVariable(name = "userId") Long targetUserId, @PathVariable(name = "ratingId") Long reviewId, Authentication authentication) {
        String userName = authentication.getName();
        String result = memberReviewService.delete(targetUserId, reviewId, userName);
        return Response.success(result);
    }

}
