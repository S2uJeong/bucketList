package com.team9.bucket_list.repository;

import com.team9.bucket_list.domain.entity.Member;
import com.team9.bucket_list.domain.entity.MemberReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberReviewRepository extends JpaRepository<MemberReview, Long> {
    Page<MemberReview> findAllByMember(Member member, Pageable pageable);

    Optional<MemberReview> findByMember_Id(Long targetMemberId);

    List<MemberReview> findAllByMember_Id(Long targetMemberId);

    Optional<MemberReview> findByMember_UserNameAndWriterId(String targetMemberName, Long fromMemberId);

    Optional<MemberReview> findByMember_IdAndWriterId(Long memberId, Long writerId);

    @Query("select coalesce(avg(mr.rate),0) from MemberReview mr where mr.member.id =:memberId")
    double averageByMemberId(@Param("memberId") Long memberId);

}
