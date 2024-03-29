package com.team9.bucket_list.config;

import com.team9.bucket_list.domain.entity.Application;
import com.team9.bucket_list.domain.entity.Member;
import com.team9.bucket_list.domain.entity.MemberReview;
import com.team9.bucket_list.domain.entity.Post;
import com.team9.bucket_list.domain.enumerate.PostStatus;
import com.team9.bucket_list.repository.ApplicationRepository;
import com.team9.bucket_list.repository.MemberReviewRepository;
import com.team9.bucket_list.repository.PostRepository;
import com.team9.bucket_list.service.AlarmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class SchedulerConfig {

    private final PostRepository postRepository;
    private final ApplicationRepository applicationRepository;
    private final MemberReviewRepository memberReviewRepository;
    private final AlarmService alarmService;

    @Scheduled(cron = "0 0 0 * * *")
    public void run() {
        LocalDate seoulNow = LocalDate.now(ZoneId.of("Asia/Seoul")).minusDays(1);
        List<String> seoulDate = List.of(seoulNow.toString().split("-"));
        String searchDate = seoulDate.get(1) + "/" + seoulDate.get(2) + "/" +seoulDate.get(0);

        List<Post> posts = postRepository.findByEventEnd(searchDate);

        for (Post post : posts) {
            post.setStatus(PostStatus.COMPLETE);
            postRepository.save(post);

            Set<Application> applications = applicationRepository.findByPost_IdAndStatus(post.getId(), (byte) 1);
            Set<Long> joinMembersId = applications.stream().map(Application::getMember).map(Member::getId).collect(Collectors.toSet());
            joinMembersId.add(post.getMember().getId());
            for (Long memberId1 : joinMembersId) {
                for (Long memberId2 : joinMembersId) {
                    if(memberId1 == memberId2) {    // 버킷 리뷰
                        alarmService.sendBucketListReviewAlarm(memberId1,post.getId(),(byte) 5);
                        //alarmService.sendAlarm(memberId1, post.getId(), (byte) 5);
                    } else{                         // 멤버 리뷰
                        // 이미 리뷰가 존재한다면 스킵
                        Optional<MemberReview> memberReview = memberReviewRepository.findByMember_IdAndWriterId(memberId1, memberId2);
                        if(memberReview.isEmpty()){
                            alarmService.sendMemberReviewAlarm(memberId1,memberId2,(byte) 4);
                        }
                        //alarmService.sendAlarm(memberId1, memberId2, (byte) 4);
                    }
                }
            }
        }

        log.info("진행 완료 변경 및 리뷰 요청 알람 전송 complete");
    }
}
