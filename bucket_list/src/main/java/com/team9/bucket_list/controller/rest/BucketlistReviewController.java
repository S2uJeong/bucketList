package com.team9.bucket_list.controller.rest;

import com.team9.bucket_list.domain.Response;
import com.team9.bucket_list.domain.dto.bucketlistReview.BucketlistReviewRequest;
import com.team9.bucket_list.domain.dto.bucketlistReview.BucketlistReviewDto;
import com.team9.bucket_list.domain.entity.BucketlistReview;
import com.team9.bucket_list.service.BucketlistReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/post")
public class BucketlistReviewController {

    private final BucketlistReviewService bucketlistReviewService;

    @GetMapping("/{postId}/review")
    public Page<BucketlistReview> reviewList(@PathVariable Long postId, @PageableDefault(sort = "createdAt", size = 20, direction = Sort.Direction.DESC) Pageable pageable) {
        Page<BucketlistReview> bucketlistReviews =  bucketlistReviewService.list(postId, pageable);
        return bucketlistReviews;
    }

    @PostMapping("/{postId}/review")
    public Response<String> reviewCreate(@PathVariable Long postId, Authentication authentication, BucketlistReviewRequest bucketlistReviewRequest) {
        String userName = authentication.getName();
        String result = bucketlistReviewService.create(postId, userName, bucketlistReviewRequest);
        return Response.success(result);
    }

    @PutMapping("{postId}/{reviewId}")
    public Response<String> reviewUpdate(@PathVariable Long postId, @PathVariable Long reviewId, Authentication authentication, BucketlistReviewRequest bucketlistReviewRequest) {
        String userName = authentication.getName();
        String result = bucketlistReviewService.update(postId, reviewId, userName, bucketlistReviewRequest);
        return Response.success(result);
    }

    @DeleteMapping("{postId}/{reviewId}")
    public Response<String> reviewDelete(@PathVariable Long postId, @PathVariable Long reviewId, Authentication authentication) {
        String userName = authentication.getName();
        String result = bucketlistReviewService.delete(postId, reviewId, userName);
        return Response.success(result);
    }


}
