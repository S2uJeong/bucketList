package com.team9.bucket_list.repository;

import com.team9.bucket_list.domain.entity.Bucketlist;
import com.team9.bucket_list.domain.entity.BucketlistReview;
import com.team9.bucket_list.domain.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BucketlistReviewRepository extends JpaRepository<BucketlistReview, Long> {
//    Page<BucketlistReview> findAllBucketlist(Bucketlist bucketlist, Pageable pageable);
    Page<BucketlistReview> findAllByPost(Post post, Pageable pageable);

    Optional<BucketlistReview> findByWriterIdAndPost_Id(Long fromMemberId, Long postId);
}
