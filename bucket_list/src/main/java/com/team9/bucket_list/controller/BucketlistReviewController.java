package com.team9.bucket_list.controller;

import com.team9.bucket_list.domain.dto.bucketlistReview.BucketlistReviewRequest;
import com.team9.bucket_list.domain.dto.bucketlistReview.BucketlistReviewDto;
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

    // post에 대한 평가 확인
    @GetMapping("/{postId}")
    public Page<BucketlistReviewDto> ratingList(@PathVariable Long postId, @PageableDefault(sort = "createdAt", size = 20, direction = Sort.Direction.DESC) Pageable pageable) {
        Page<BucketlistReviewDto> ratingDtoPage =  bucketlistReviewService.ratingList(postId, pageable);
        return ratingDtoPage;
    }

    // post에 대한 평가 작성
    @PostMapping("/{postId}")
    public String ratingCreate(@PathVariable Long postId, BucketlistReviewRequest bucketlistReviewRequest) {
        return bucketlistReviewService.create(postId, bucketlistReviewRequest);
    }

    // post에 대한 평가 수정 - 근데 평가 수정이 필요한가?

    // post에 대한 평가 삭제
    @DeleteMapping("{postId}/{ratingId}")
    public String ratingUpdate(@PathVariable Long postId, @PathVariable Long ratingId) {
        return bucketlistReviewService.delete(postId, ratingId);
    }


}
