package com.team9.bucket_list.controller.rest;

import com.team9.bucket_list.domain.Response;
import com.team9.bucket_list.domain.dto.bucketlistReview.BucketlistReviewRequest;
import com.team9.bucket_list.domain.dto.bucketlistReview.BucketlistReviewResponse;
import com.team9.bucket_list.service.BucketlistReviewService;
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

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/post/rating")
@Tag(name = "버킷리스트 리뷰", description = "버킷리스트(게시글)에 참여한 사람들은 리뷰를 작성할 수 있습니다.")
public class BucketlistReviewController {

    private final BucketlistReviewService bucketlistReviewService;

    @GetMapping("/{postId}")
    @Operation(summary = "리뷰 조회", description = "특정게시글의 리뷰를 pageable하여 출력합니다.")
    public Response<Page<BucketlistReviewResponse>> reviewList(@Parameter(name = "postId", description = "게시글 id")  @PathVariable Long postId,
                                                     @Parameter(hidden = true) @PageableDefault(sort = "createdAt", size = 5, direction = Sort.Direction.DESC) Pageable pageable) {
        Page<BucketlistReviewResponse> bucketlistReviewResponses =  bucketlistReviewService.list(postId, pageable);
        return Response.success(bucketlistReviewResponses);
    }

    @PostMapping()
    @Operation(summary = "리뷰 작성", description = "특정게시글에 리뷰를 작성합니다.")
    public Response<String> reviewCreate(Authentication authentication, @RequestBody BucketlistReviewRequest bucketlistReviewRequest) {
        Long memberId = Long.valueOf(authentication.getName());
        String result = bucketlistReviewService.create(memberId, bucketlistReviewRequest);
        return Response.success(result);
    }

}
