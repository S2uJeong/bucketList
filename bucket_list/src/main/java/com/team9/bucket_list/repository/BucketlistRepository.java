package com.team9.bucket_list.repository;

import com.team9.bucket_list.domain.entity.Bucketlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BucketlistRepository extends JpaRepository<Bucketlist, Long> {
    Optional<Bucketlist> findByPostId(Long postId);
}
