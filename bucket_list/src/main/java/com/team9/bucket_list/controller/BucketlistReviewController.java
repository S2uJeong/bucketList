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

    @GetMapping("/{userId}")
    public Page<BucketlistReviewDto> ratingList(@PathVariable Long userId, @PageableDefault(sort = "createdAt", size = 20, direction = Sort.Direction.DESC) Pageable pageable) {
        Page<BucketlistReviewDto> ratingDtoPage =  bucketlistReviewService.ratingList(userId, pageable);
        return ratingDtoPage;
    }

    @PostMapping("/{userId}")
    public void ratingCreate(@PathVariable Long userId, BucketlistReviewRequest bucketlistReviewRequest) {
        bucketlistReviewService.create(userId, bucketlistReviewRequest);
    }


}
