package com.team9.bucket_list.repository;

import com.team9.bucket_list.domain.entity.Application;
import com.team9.bucket_list.domain.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    List<Application> findAllByMemberIdAndPostId(Long memberId, Long postId);

    @Query(value = "SELECT application " +
            "FROM Application application " +
            "WHERE application.status = 0 " +
            "AND application.post.id = :postId")
    List<Application> findAllNotDicision(Long postId);

    Page<Application> findAllByPost_IdAndStatusContains(Long postId, byte status, Pageable pageable);

    /*@Modifying(clearAutomatically = true)
    @Query("update Application a set a.status = :status where a.id = :id")
    int updateApplicationStatus(Long postId, byte status);*/

    int countByPost_IdAndStatusContains(Long postId, byte status);

    Set<Application> findByMember_IdAndStatus(Long memberId, byte status);

    @Modifying(clearAutomatically = true)
    @Query(value = "update application a set a.status = 2 where a.member_id = :memberId and a.post_id = :postId and a.status = 1 ;",nativeQuery = true)
    int updateRejectApplication(@Param("memberId") Long memberId, @Param("postId") Long postId);

    Set<Application> findByPost_IdAndStatus(Long postId, byte status);
}
