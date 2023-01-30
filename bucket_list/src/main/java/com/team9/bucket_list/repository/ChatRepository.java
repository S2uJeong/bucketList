package com.team9.bucket_list.repository;

import com.team9.bucket_list.domain.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long> {
}
