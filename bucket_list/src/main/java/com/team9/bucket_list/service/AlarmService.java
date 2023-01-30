package com.team9.bucket_list.service;

import com.team9.bucket_list.domain.dto.alarm.AlarmListResponse;
import com.team9.bucket_list.domain.entity.Alarm;
import com.team9.bucket_list.domain.entity.Member;
import com.team9.bucket_list.domain.entity.Post;
import com.team9.bucket_list.execption.ApplicationException;
import com.team9.bucket_list.execption.ErrorCode;
import com.team9.bucket_list.repository.AlarmRepository;
import com.team9.bucket_list.repository.MemberRepository;
import com.team9.bucket_list.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@EnableAsync
@Slf4j
public class AlarmService {

    private final AlarmRepository alarmRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    public Page<AlarmListResponse> alarmList(Pageable pageable, Long memberId) {
        return AlarmListResponse.toList(alarmRepository.findAllByMember_IdAndReadStatusContains(memberId,pageable, (byte)0));
    }

    @Async
    public void sendAlarm(Long senderId, Long postId, byte category) {
        Member sender = memberRepository.findById(senderId).orElseThrow( () -> new ApplicationException(ErrorCode.USERNAME_NOT_FOUNDED));

        Post post = postRepository.findById(postId).orElseThrow( () -> new ApplicationException(ErrorCode.POST_NOT_FOUND));

        Member member = memberRepository.findById(post.getMember().getId()).orElseThrow( () -> new ApplicationException(ErrorCode.USERNAME_NOT_FOUNDED));

        alarmRepository.save(Alarm.save(category,member,postId, sender.getUserName()));
    }


    public int alarmCount(Long memberId) {
        return alarmRepository.countByMember_Id(memberId);
    }
}
