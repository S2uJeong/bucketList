package com.team9.bucket_list.repository;

import com.team9.bucket_list.domain.entity.Member;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    
    Optional<Member> findByOauthId(String oAuthId);

    @Query("select m from Member m where m.id in :memberId")
    List<Member> findAllMemberIdIn(List<Long> memberId);
}
