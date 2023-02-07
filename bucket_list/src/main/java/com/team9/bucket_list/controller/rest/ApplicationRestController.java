package com.team9.bucket_list.controller.rest;

import com.team9.bucket_list.domain.Response;
import com.team9.bucket_list.domain.dto.application.ApplicationDecisionRequest;
import com.team9.bucket_list.domain.dto.application.ApplicationFindAllResponse;
import com.team9.bucket_list.domain.dto.application.ApplicationListResponse;
import com.team9.bucket_list.domain.dto.application.ApplicationRequest;
import com.team9.bucket_list.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/application-rest")
public class ApplicationRestController {

    private final ApplicationService applicationService;

    //신청서 제출
    @PostMapping
    public Response getApplication(@RequestBody ApplicationRequest applicationRequest, Authentication authentication) {
        //memberId는 jwt에서 가져옴
        Long memberId = Long.parseLong(authentication.getName());
        log.info("app content : " + applicationRequest.getContent() + "app postId : " + applicationRequest.getPostId());
        applicationService.getApplication(applicationRequest,memberId);
        return Response.success("신청서 제출 성공");
    }

    //특정 포스트의 신청서 리스트 전체 출력
    @GetMapping("/{postId}")
    public Response<ApplicationFindAllResponse> applicationListAll(@PathVariable Long postId, Authentication authentication) {
        //memberId는 jwt에서 가져옴
        Long memberId = Long.parseLong(authentication.getName());
        List<ApplicationListResponse> all = applicationService.getNotDicisionList(postId, memberId);
        return Response.success(new ApplicationFindAllResponse(all, all.size()));
    }

    //특정 포스트의 신청서 리스트 (승낙, 거절 제외), 글쓴이만 확인 가능
    @GetMapping("/{postId}/{statusCode}")
    public Page<ApplicationListResponse> applicationList(@PathVariable Long postId, @PathVariable byte statusCode, Pageable pageable) {
        //memberId는 jwt에서 가져옴
        Long memberId = 1L;
        return applicationService.applicationList(postId, memberId, statusCode, pageable);
    }

    //특정 포스트의 승낙된 신청서 리스트, 글쓴이만 확인 가능
/*    @GetMapping("/{postId}/accept")
    public Page<ApplicationListResponse> applicationAcceptList(@PathVariable Long postId) {
        //memberId는 jwt에서 가져옴
        Long memberId = 1L;
        return applicationService.applicationList(postId, memberId, (byte) 1);
    }*/

    //특정포스트의 신청서 수락 or 거절하기
    @PutMapping("/{postId}")
    public Response applicationDecision(@PathVariable Long postId, @RequestBody ApplicationDecisionRequest applicationDecisionRequest, Authentication authentication) {
        //memberId는 jwt에서 가져옴
        Long memberId = Long.parseLong(authentication.getName());
        return Response.success(applicationService.applicationDecision(postId, memberId, applicationDecisionRequest));
    }

    //승낙된 인원 조회
    @GetMapping("/{postId}/count")
    public Response applicationAcceptCount(@PathVariable Long postId) {
        return Response.success(applicationService.applicationAcceptCount(postId));
    }
}
