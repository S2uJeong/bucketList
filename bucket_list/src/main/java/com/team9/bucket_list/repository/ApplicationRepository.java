package com.team9.bucket_list.repository;

import com.team9.bucket_list.domain.entity.Application;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    Page<Application> findAllByPost_IdAndStatusContains(Long postId, byte status, Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Query("update Application a set a.status = :status where a.id = :id")
    int updateApplicationStatus(Long postId, byte status);

    int countByPost_IdAndStatusContains(Long postId, byte status);

    Set<Application> findByMember_Id(Long memberId);
}
