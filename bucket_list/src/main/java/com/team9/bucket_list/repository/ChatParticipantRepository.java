package com.team9.bucket_list.repository;

import com.team9.bucket_list.domain.entity.ChatParticipant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, Long> {
    List<ChatParticipant> findAllByMember_Id(Long memberId);

    Page<ChatParticipant> findAllByChatRoom_Id(Long roomId, Pageable pageable);

    Optional<ChatParticipant> findByChatRoom_IdAndMember_Id(Long chatRoomId, Long memberId);

    List<ChatParticipant> findAllByChatRoom_Id(Long roomId);

    void deleteByChatRoom_IdAndMember_Id(Long chatRoomId, Long memberId);
}
