package com.team9.bucket_list.repository;

import com.team9.bucket_list.domain.entity.Post;

import com.team9.bucket_list.domain.enumerate.PostStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findById (Long id);

    @Query("select p.member.id from Post p where p.member.id = :memberId")
    Long findByMemberId(@Param("memberId") Long memberId);

    Page<Post> findByMember_Id(Long memberId, Pageable pageable);

    Page<Post> findByIdIn(Set<Long> postId, Pageable pageable);

    Page<Post> findByIdInAndStatus(Set<Long> postId, PostStatus status, Pageable pageable);

    Page<Post> findByCategory(String category, Pageable pageable);
}
