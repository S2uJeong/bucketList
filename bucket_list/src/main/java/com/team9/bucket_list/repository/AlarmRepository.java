package com.team9.bucket_list.repository;

import com.team9.bucket_list.domain.entity.Alarm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {

    Page<Alarm> findAllByMember_IdAndReadStatusContains(Long memberId, Pageable pageable, byte readStatus);

    int countByMember_Id(Long memberId);

    /*@Modifying(clearAutomatically = true)
    @Query("update Alarm a set a.readStatus = 1 where a.id = :alarmId")
    int updateAlarm(Long alarmId);*/
}
