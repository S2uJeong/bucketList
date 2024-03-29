package com.team9.bucket_list.service;

import com.team9.bucket_list.domain.dto.application.ApplicationAcceptCountResponse;
import com.team9.bucket_list.domain.dto.application.ApplicationDecisionRequest;
import com.team9.bucket_list.domain.dto.application.ApplicationListResponse;
import com.team9.bucket_list.domain.dto.application.ApplicationRequest;
import com.team9.bucket_list.domain.entity.Application;
import com.team9.bucket_list.domain.entity.ChatParticipant;
import com.team9.bucket_list.domain.entity.Member;
import com.team9.bucket_list.domain.entity.Post;
import com.team9.bucket_list.execption.ApplicationException;
import com.team9.bucket_list.execption.ErrorCode;
import com.team9.bucket_list.repository.ApplicationRepository;
import com.team9.bucket_list.repository.ChatParticipantRepository;
import com.team9.bucket_list.repository.MemberRepository;
import com.team9.bucket_list.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApplicationService {

    private final ChatParticipantRepository chatParticipantRepository;
    private final ApplicationRepository applicationRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final AlarmService alarmService;

    public boolean canWriteApplication(Authentication authentication, Long postId) throws ParseException {
        Long memberId = Long.parseLong(authentication.getName());
        List<Application> optionalApplication = applicationRepository.findAllByMemberIdAndPostId(memberId, postId);

        //ddd
        Post post = postRepository.findById(postId).get();
        String untilRecruit = post.getUntilRecruit();
        log.info("마감 날짜 = {}",untilRecruit); //mm/dd/yyyy
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        Date endDate = formatter.parse(untilRecruit);
        Date now = new Date();
        if (now.before(endDate)) {
            //모집 마감일 전
            log.info("참");
        } else {
            //모집 마감일 후
            log.info("거짓");
        }
        ///

        return optionalApplication.isEmpty();
    }

    public void getApplication(ApplicationRequest applicationRequest,Long memberId) {
        Post post = findPostById(applicationRequest.getPostId());
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new ApplicationException(ErrorCode.USERNAME_NOT_FOUNDED));
        applicationRepository.save(Application.save(applicationRequest,post,member));

        //알람 발생
        alarmService.sendAlarm2(member.getId(),post.getMember().getId(), post.getId(), (byte) 2);
        //alarmService.sendAlarm(member.getId(),post.getId(),(byte)2);
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

        //확정된 인원 == 최대 인원 AND 수락 요청이면 예외 발생
        if ((post.getPermitNum() == post.getEntrantNum()) && (applicationDecisionRequest.getStatus()==1)) {
            throw new ApplicationException(ErrorCode.EXCEED_ENTRANT_NUM);
        }

        //신청서 존재 확인
        Application application = applicationRepository.findById(applicationDecisionRequest.getApplicationId()).orElseThrow(() -> new ApplicationException(ErrorCode.APPLICATION_NOT_FOUND));

        //업데이트
        applicationRepository.save(Application.updateStatus(application,applicationDecisionRequest));

        // 승인인 경우 chat_participant 추가
        if (applicationDecisionRequest.getStatus() == 1) {
            ChatParticipant chatParticipant = ChatParticipant.builder()
                    .chatRoom(post.getChatRoom())
                    .member(application.getMember())
                    .build();
            chatParticipantRepository.save(chatParticipant);
        }

        // 인원 꽉 차면 모집 완료로 업데이트
        if (post.getPermitNum() == post.getEntrantNum()) {
            post.setRecruitComplete();
        }

        //알람 발생
        if(applicationDecisionRequest.getStatus() == (byte)1)
            alarmService.sendAlarm2(memberId,application.getMember().getId(),post.getId(), (byte)3);

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
