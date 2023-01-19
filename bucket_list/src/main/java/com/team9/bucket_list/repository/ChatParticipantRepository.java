package com.team9.bucket_list.repository;

import com.team9.bucket_list.domain.entity.ChatParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, Long> {
    Set<ChatParticipant> findAllByMember_Id(Long memberId);
}
