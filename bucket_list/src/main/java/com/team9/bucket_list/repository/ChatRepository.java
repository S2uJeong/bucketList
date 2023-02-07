package com.team9.bucket_list.repository;

import com.team9.bucket_list.domain.entity.Chat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    Page<Chat> findAllByChatRoom_Id(Long chatRoomId, Pageable pageable);
}
