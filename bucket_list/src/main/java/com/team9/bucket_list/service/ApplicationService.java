package com.team9.bucket_list.service;

import com.team9.bucket_list.domain.dto.application.ApplicationAcceptCountResponse;
import com.team9.bucket_list.domain.dto.application.ApplicationDecisionRequest;
import com.team9.bucket_list.domain.dto.application.ApplicationListResponse;
import com.team9.bucket_list.domain.dto.application.ApplicationRequest;
import com.team9.bucket_list.domain.entity.Application;
import com.team9.bucket_list.domain.entity.Member;
import com.team9.bucket_list.domain.entity.Post;
import com.team9.bucket_list.execption.ApplicationException;
import com.team9.bucket_list.execption.ErrorCode;
import com.team9.bucket_list.repository.ApplicationRepository;
import com.team9.bucket_list.repository.MemberRepository;
import com.team9.bucket_list.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final AlarmService alarmService;

    public void getApplication(ApplicationRequest applicationRequest,Long memberId) {
        Post post = findPostById(applicationRequest.getPostId());
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new ApplicationException(ErrorCode.USERNAME_NOT_FOUNDED));
        applicationRepository.save(Application.save(applicationRequest,post,member));

        //알람 발생
        alarmService.sendAlarm(member.getId(),post.getId(),(byte)2);
    }

    public List<ApplicationListResponse> getNotDicisionList(Long postId, Long memberId) {
        //해당 게시글의 작성자 아이디와 로그인된 아이디를 비교
        checkMemberIdInPost(memberId);

        //post 존재 확인
        findPostById(postId);
        List<Application> allByPostId = applicationRepository.findAllNotDicision(postId);
        return ApplicationListResponse.applicationListResponseList(allByPostId);
    }

    public Page<ApplicationListResponse> applicationList(Long postId, Long memberId, byte status, Pageable pageable) {
        //해당 게시글의 작성자 아이디와 로그인된 아이디를 비교 아닐경우 예외 발생
        checkMemberIdInPost(memberId);

        //post존재 확인
        findPostById(postId);

        return ApplicationListResponse.pageList(applicationRepository.findAllByPost_IdAndStatusContains(postId, status,pageable));
    }

    @Transactional
    public int applicationDecision(Long postId, Long memberId, ApplicationDecisionRequest applicationDecisionRequest) {
        //해당 게시글의 작성자 아이디와 로그인된 아이디를 비교 아닐경우 예외 발생
        checkMemberIdInPost(memberId);
        //post존재 확인
        Post post = findPostById(postId);
        log.info("permitnum ={}",post.getPermitNum());
        //확정된 인원 == 최대 인원이면 예외 발생
        if (post.getPermitNum() == post.getEntrantNum()) {
            throw new ApplicationException(ErrorCode.EXCEED_ENTRANT_NUM);
        }

        //신청서 존재 확인
        Application application = applicationRepository.findById(applicationDecisionRequest.getApplicationId()).orElseThrow(() -> new ApplicationException(ErrorCode.APPLICATION_NOT_FOUND));

        //업데이트
        applicationRepository.save(Application.updateStatus(application,applicationDecisionRequest));

        //
        if (post.getPermitNum() == post.getEntrantNum()) {
            post.setRecruitComplete();
        }

        //알람 발생
        if(applicationDecisionRequest.getStatus() == (byte)1)
            alarmService.sendAlarm(memberId,post.getId(),(byte)3);

        return 1;
    }

    public ApplicationAcceptCountResponse applicationAcceptCount(Long postId) {
        postRepository.findById(postId).orElseThrow(() -> new ApplicationException(ErrorCode.POST_NOT_FOUND));

        int count = applicationRepository.countByPost_IdAndStatusContains(postId,(byte) 1);
        return ApplicationAcceptCountResponse.response(postId,count);
    }

    public Post findPostById(Long postId) {
        return postRepository.findById(postId).orElseThrow(() -> new ApplicationException(ErrorCode.POST_NOT_FOUND));
    }

    public boolean checkMemberIdInPost(Long memberId) {
        if(!(memberId == postRepository.findByMemberId(memberId))) throw new ApplicationException(ErrorCode.USERNAME_NOT_FOUNDED);
        else return true;
    }

}
