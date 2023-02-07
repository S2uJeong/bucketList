package com.team9.bucket_list.controller.rest;

import com.team9.bucket_list.domain.Response;
import com.team9.bucket_list.domain.dto.bucketlistReview.BucketlistReviewRequest;
import com.team9.bucket_list.domain.dto.bucketlistReview.BucketlistReviewDto;
import com.team9.bucket_list.domain.entity.BucketlistReview;
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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/post")
@Tag(name = "버킷리스트 리뷰", description = "버킷리스트(게시글)에 참여한 사람들은 리뷰를 작성할 수 있습니다.")
public class BucketlistReviewController {

    private final BucketlistReviewService bucketlistReviewService;

    @GetMapping("/{postId}/review")
    @Operation(summary = "리뷰 조회", description = "특정게시글의 리뷰를 pageable하여 출력합니다.")
    public Page<BucketlistReview> reviewList(@Parameter(name = "postId", description = "게시글 id")  @PathVariable Long postId,
                                             @Parameter(hidden = true) @PageableDefault(sort = "createdAt", size = 20, direction = Sort.Direction.DESC) Pageable pageable) {
        Page<BucketlistReview> bucketlistReviews =  bucketlistReviewService.list(postId, pageable);
        return bucketlistReviews;
    }

    @PostMapping("/{postId}/review")
    @Operation(summary = "리뷰 작성", description = "특정게시글에 리뷰를 작성합니다.")
    public Response<String> reviewCreate(@Parameter(name = "postId", description = "게시글 id") @PathVariable Long postId,
                                         Authentication authentication,
                                         @RequestBody BucketlistReviewRequest bucketlistReviewRequest) {
        String userName = authentication.getName();
        String result = bucketlistReviewService.create(postId, userName, bucketlistReviewRequest);
        return Response.success(result);
    }

    @PutMapping("{postId}/{reviewId}")
    @Operation(summary = "리뷰 수정", description = "리뷰를 수정합니다.")
    public Response<String> reviewUpdate(@Parameter(name = "postId", description = "게시글 id") @PathVariable Long postId,
                                         @Parameter(name = "reviewId", description = "리뷰 id") @PathVariable Long reviewId, Authentication authentication,
                                         @RequestBody BucketlistReviewRequest bucketlistReviewRequest) {
        String userName = authentication.getName();
        String result = bucketlistReviewService.update(postId, reviewId, userName, bucketlistReviewRequest);
        return Response.success(result);
    }

    @DeleteMapping("{postId}/{reviewId}")
    @Operation(summary = "리뷰 삭제", description = "리뷰를 삭제합니다.")
    public Response<String> reviewDelete(@Parameter(name = "postId", description = "게시글 id") @PathVariable Long postId,
                                         @Parameter(name = "reviewId", description = "리뷰 id") @PathVariable Long reviewId,
                                         Authentication authentication) {
        String userName = authentication.getName();
        String result = bucketlistReviewService.delete(postId, reviewId, userName);
        return Response.success(result);
    }


}
