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
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    public void getApplication(ApplicationRequest applicationRequest,Long memberId) {
        Post post = findPostById(applicationRequest.getPostId());
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new ApplicationException(ErrorCode.USERNAME_NOT_FOUNDED));
        applicationRepository.save(Application.save(applicationRequest,post,member));
    }

    public Page<ApplicationListResponse> applicationList(Long postId, Long memberId, byte status) {
        //해당 게시글의 작성자 아이디와 로그인된 아이디를 비교 아닐경우 예외 발생
        checkMemberIdInPost(memberId);

        //post존재 확인
        findPostById(postId);

        return ApplicationListResponse.pageList(applicationRepository.findAllByPost_IdAndStatusContains(postId, status));
    }

    public int applicationDecision(Long postId, Long memberId, ApplicationDecisionRequest applicationDecisionRequest) {
        //해당 게시글의 작성자 아이디와 로그인된 아이디를 비교 아닐경우 예외 발생
        checkMemberIdInPost(memberId);
        //post존재 확인
        findPostById(postId);
        //신청서 존재 확인
        applicationRepository.findById(applicationDecisionRequest.getId()).orElseThrow(() -> new ApplicationException(ErrorCode.APPLICATION_NOT_FOUND));

        //업데이트
        return applicationRepository.updateApplicationStatus(applicationDecisionRequest.getId(),applicationDecisionRequest.getStatus());
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
