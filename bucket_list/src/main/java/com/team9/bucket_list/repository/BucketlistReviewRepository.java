package com.team9.bucket_list.repository;

import com.team9.bucket_list.domain.entity.BucketlistReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BucketlistReviewRepository extends JpaRepository<BucketlistReview, Long> {
}
