package com.team9.bucket_list.controller.rest;

import com.team9.bucket_list.domain.Response;
import com.team9.bucket_list.domain.dto.memberReview.MemberReviewRequest;
import com.team9.bucket_list.domain.dto.memberReview.MemberReviewResponse;
import com.team9.bucket_list.domain.entity.Member;
import com.team9.bucket_list.service.MemberReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/member/rating")
@RequiredArgsConstructor
@Tag(name = "멤버 평가", description = "버킷리스트에 참가했던 멤버를 평가할 수 있습니다. 해당 평가 평점은 프로필에 표시됩니다.")
public class MemeberReviewController {

    private final MemberReviewService memberReviewService;

    @GetMapping("/{targerUserId}")
    @Operation(summary = "평가 조회", description = "특정 id의 멤버에 대한 평가를 조회합니다.")
    public Page<MemberReviewResponse> list(@Parameter(name = "targetMemberName", description = "평가 대상 멤버의 name") @PathVariable String targerUserName,
                                           @Parameter(hidden = true) @PageableDefault(sort = "createdAt", size = 10, direction = Sort.Direction.DESC) Pageable pageable) {
        Page<MemberReviewResponse> memberReviewResponses = memberReviewService.list(targerUserName, pageable)
                .map(memberReview -> MemberReviewResponse.response(memberReview));
        return memberReviewResponses;
    }

    @PostMapping()
    @Operation(summary = "평가 작성", description = "특정 id의 멤버에 대한 평가를 작성합니다.")
    public Response<String> createRating(Authentication authentication, @RequestBody MemberReviewRequest memberReviewRequest) {
        Long memberId = Long.valueOf(authentication.getName());
        String result = memberReviewService.create(memberId, memberReviewRequest);
        return Response.success(result);
    }

}
