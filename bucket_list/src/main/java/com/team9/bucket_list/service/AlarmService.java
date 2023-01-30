package com.team9.bucket_list.service;

import com.team9.bucket_list.domain.dto.AlarmResponse;
import com.team9.bucket_list.domain.entity.Alarm;
import com.team9.bucket_list.domain.entity.Member;
import com.team9.bucket_list.execption.ApplicationException;
import com.team9.bucket_list.execption.ErrorCode;
import com.team9.bucket_list.repository.AlarmRepository;
import com.team9.bucket_list.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlarmService {

    private MemberRepository memberRepository;
    private AlarmRepository alarmRepository;

    public Page<AlarmResponse> list(Long targetMemberId, String userName, Pageable pageable) {

        Member member = memberRepository.findById(targetMemberId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.USERNAME_NOT_FOUNDED));

        if (!member.getUserName().equals(userName)) {
            throw new ApplicationException(ErrorCode.INVALID_PERMISSION);
        }

        Page<Alarm> alarms = alarmRepository.findByMemberId(member.getId(), pageable);
        Page<AlarmResponse> alarmResponses = AlarmResponse.response(alarms);

        return alarmResponses;
    }
}
