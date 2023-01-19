package com.team9.bucket_list.repository;

import com.team9.bucket_list.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
