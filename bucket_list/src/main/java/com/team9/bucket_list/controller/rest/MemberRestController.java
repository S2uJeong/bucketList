package com.team9.bucket_list.controller.rest;

import com.team9.bucket_list.domain.Response;
import com.team9.bucket_list.domain.dto.memberReview.MemberReviewRequest;
import com.team9.bucket_list.domain.dto.memberReview.MemberReviewResponse;
import com.team9.bucket_list.domain.dto.post.PostReadResponse;
import com.team9.bucket_list.service.MemberReviewService;
import com.team9.bucket_list.service.PostService;
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
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
@Tag(name = "멤버", description = "좋아요, 마이피드 기능을 포함합니다.")
public class MemberRestController {

    private final MemberReviewService memberReviewService;
    private final PostService postService;

    // 멤버 평가
    @GetMapping("/{memberId}/ratings")
    @Operation(summary = "평가 조회", description = "특정 id의 멤버에 대한 평가를 조회합니다.")
    public Response<Page<MemberReviewResponse>> list(@Parameter(name = "memberId", description = "평가 대상 멤버의 id") @PathVariable Long memberId,
                                                     @Parameter(hidden = true) @PageableDefault(sort = "createdAt", size = 4, direction = Sort.Direction.DESC) Pageable pageable) {
        Page<MemberReviewResponse> memberReviewResponses = memberReviewService.list(memberId, pageable);
        return Response.success(memberReviewResponses);
    }

    @PostMapping("/ratings")
    @Operation(summary = "평가 작성", description = "특정 id의 멤버에 대한 평가를 작성합니다.")
    public Response<String> createRating(Authentication authentication, @RequestBody MemberReviewRequest memberReviewRequest) {
        Long memberId = Long.valueOf(authentication.getName());
        String result = memberReviewService.create(memberId, memberReviewRequest);
        return Response.success(result);
    }

    //== 마이피드 ==//
    // 좋아요한
    @GetMapping("/myfeed-like")
    @Operation(summary = "마이피드 - 좋아요한 글 조회", description = "로그인 되어 있는 멤버가 좋아요한 Post 리스트를 출력합니다.")
    public Response<Page<PostReadResponse>> myFeedLike(@Parameter(hidden = true) @PageableDefault(size = 16, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
                                                       Authentication authentication) {
        Page<PostReadResponse> likePosts = postService.myFeedLike(pageable, Long.valueOf(authentication.getName()));
        return Response.success(likePosts);
    }

    // 작성한
    @GetMapping("/myfeed-create")
    @Operation(summary = "마이피드 - 작성한 글 조회", description = "로그인 되어 있는 멤버가 작성한 Post 리스트를 출력합니다.")
    public Response<Page<PostReadResponse>> myFeedCreate(@Parameter(hidden = true) @PageableDefault(size = 16, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
                                                         Authentication authentication) {
        Page<PostReadResponse> createPosts = postService.myFeedCreate(pageable, Long.valueOf(authentication.getName()));
        return Response.success(createPosts);
    }

    // 신청한
    @GetMapping("/myfeed-apply")
    @Operation(summary = "마이피드 - 참가 신청한 글 조회", description = "로그인 되어 있는 멤버가 참가 신청한 Post 리스트를 출력합니다.")
    public Response<Page<PostReadResponse>> myFeedApply(@Parameter(hidden = true) @PageableDefault(size = 16, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
                                                        Authentication authentication) {
        Page<PostReadResponse> applyPosts = postService.myFeedApply(pageable, Long.valueOf(authentication.getName()));
        return Response.success(applyPosts);
    }

    // 승낙받은
    @GetMapping("/myfeed-consent")
    @Operation(summary = "마이피드 - 승낙 받은 글 조회", description = "로그인 되어 있는 멤버가 승낙 받은 Post 리스트를 출력합니다.")
    public Response<Page<PostReadResponse>> myFeedConsent(@Parameter(hidden = true) @PageableDefault(size = 16, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
                                                          Authentication authentication) {
        Page<PostReadResponse> consentPosts = postService.myFeedConsent(pageable, Long.valueOf(authentication.getName()));
        return Response.success(consentPosts);
    }

    // 완료한
    @GetMapping("/myfeed-complete")
    @Operation(summary = "마이피드 - 잔행 완료된 글 조회", description = "로그인 되어 있는 멤버가 완료한 Post 리스트를 출력합니다.")
    public Response<Page<PostReadResponse>> myFeedComplete(@Parameter(hidden = true) @PageableDefault(size = 16, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
                                                           Authentication authentication) {
        Page<PostReadResponse> completePosts = postService.myFeedComplete(pageable, Long.valueOf(authentication.getName()));
        return Response.success(completePosts);
    }
}
