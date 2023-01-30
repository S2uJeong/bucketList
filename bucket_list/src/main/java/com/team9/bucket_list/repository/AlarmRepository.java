package com.team9.bucket_list.repository;

import com.team9.bucket_list.domain.entity.Alarm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {
    Page<Alarm> findByMemberId(Long id, Pageable pageable);
}
