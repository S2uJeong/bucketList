package com.team9.bucket_list.controller.rest;

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
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/rating")
public class BucketlistReviewController {

    private final BucketlistReviewService bucketlistReviewService;

    // 해당 Bucketlist에 대한 평가 리스트
    @GetMapping("/{postId}")
    public Page<BucketlistReview> ratingList(@PathVariable Long postId, @PageableDefault(sort = "createdAt", size = 20, direction = Sort.Direction.DESC) Pageable pageable) {
        Page<BucketlistReview> bucketlistReviews =  bucketlistReviewService.ratingList(postId, pageable);
        return bucketlistReviews;
    }

    // 해당 Bucketlist에 대한 평가 확인
    @GetMapping("/{postId}/{ratingId}")
    public BucketlistReviewDto ratingDetail(@PathVariable Long postId, @PathVariable Long ratingId, @PageableDefault(sort = "createdAt", size = 20, direction = Sort.Direction.DESC) Pageable pageable) {
        BucketlistReviewDto ratingDtoPage =  bucketlistReviewService.detail(postId, ratingId, pageable);
        return ratingDtoPage;
    }

    // 해당 Bucketlist에 대한 평가 작성
    @PostMapping("/{postId}")
    public BucketlistReviewDto ratingCreate(@PathVariable Long postId, BucketlistReviewRequest bucketlistReviewRequest) {
        BucketlistReviewDto bucketlistReviewDto = bucketlistReviewService.create(postId, bucketlistReviewRequest);
        return bucketlistReviewDto;
    }

    // 해당 Bucketlist에 대한 평가 수정 - 근데 평가 수정이 필요한가?
    @PutMapping("{postId}/{ratingId}")
    public String ratingUpdate(@PathVariable Long postId, @PathVariable Long ratingId, BucketlistReviewRequest bucketlistReviewRequest) {
        return bucketlistReviewService.update(postId, ratingId, bucketlistReviewRequest);
    }

    // 해당 Bucketlist에 대한 평가 삭제
    @DeleteMapping("{postId}/{ratingId}")
    public String ratingDelete(@PathVariable Long postId, @PathVariable Long ratingId) {
        return bucketlistReviewService.delete(postId, ratingId);
    }


}
