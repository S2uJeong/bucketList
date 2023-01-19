package com.team9.bucket_list.repository;

import com.team9.bucket_list.domain.entity.Bucketlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BucketlistRepository extends JpaRepository<Bucketlist, Long> {
}
