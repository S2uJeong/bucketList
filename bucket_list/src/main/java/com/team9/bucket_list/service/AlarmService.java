package com.team9.bucket_list.service;

import com.team9.bucket_list.domain.dto.alarm.AlarmListResponse;
import com.team9.bucket_list.repository.AlarmRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlarmService {

    private final AlarmRepository alarmRepository;

    public Page<AlarmListResponse> alarmList(Pageable pageable, Long memberId) {
        return AlarmListResponse.toList(alarmRepository.findAllByMember_IdAndReadStatusContains(memberId,pageable, (byte)0));
    }
}
