package com.team9.bucket_list.repository;

import com.team9.bucket_list.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
