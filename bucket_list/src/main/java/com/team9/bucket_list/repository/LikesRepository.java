package com.team9.bucket_list.repository;

import com.team9.bucket_list.domain.entity.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;


public interface LikesRepository extends JpaRepository<Likes, Long> {
    Optional<Likes> findByPost_IdAndMember_Id(Long postId, Long memberId);
    void  deleteByPost_IdAndMember_Id(Long postId, Long memberId);
    Long countByPostId(Long postId);

    Set<Likes> findByMember_Id(Long memberId);
}
