package com.team9.bucket_list.controller.rest;

import com.team9.bucket_list.domain.Response;
import com.team9.bucket_list.domain.dto.application.ApplicationDecisionRequest;
import com.team9.bucket_list.domain.dto.application.ApplicationFindAllResponse;
import com.team9.bucket_list.domain.dto.application.ApplicationListResponse;
import com.team9.bucket_list.domain.dto.application.ApplicationRequest;
import com.team9.bucket_list.service.ApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "버킷리스트 참가신청서", description = "버킷리스트(게시글)에 참가신청을 합니다.")
public class ApplicationRestController {

    private final ApplicationService applicationService;

    //신청서 제출
    @PostMapping
    @Operation(summary = "신청서 제출", description = "로그인 한 member가 applicationRequest(Dto)을 통해 post한다.")
    public Response getApplication(@Parameter(required = true) @RequestBody ApplicationRequest applicationRequest,
                                   Authentication authentication) {
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
    @Operation(summary = "신청서 리스트 조회", description = "postId를 통해 특정한 게시글의 신청서 리스트를 확인한다. statusCode별로 조회하며, 게시글 작성자만 확인 가능하다.")
    public Page<ApplicationListResponse> applicationList(@Parameter(name = "postId", description = "게시글 id", required = true, in = ParameterIn.PATH) @PathVariable Long postId,
                                                         @Parameter(name = "statusCode", description = "신청서 상태코드 - [ 선택안함 : 0, 승낙 : 1, 거절 : 2 ]", required = true, in = ParameterIn.PATH)
                                                         @PathVariable byte statusCode,
                                                         @Parameter(hidden = true) Pageable pageable) {
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
    @Operation(summary = "신청서 수락/거절", description = "게시글 작성자가 신청서를 수락/거절합니다.")
    public Response applicationDecision(@Parameter(name = "postId", description = "게시글 id", in = ParameterIn.PATH) @PathVariable Long postId,
                                        @Parameter @RequestBody ApplicationDecisionRequest applicationDecisionRequest, Authentication authentication) {
        log.info("APPLICATION SERVICE DECISION");
        //memberId는 jwt에서 가져옴
        Long memberId = Long.parseLong(authentication.getName());
        return Response.success(applicationService.applicationDecision(postId, memberId, applicationDecisionRequest));
    }

    //승낙된 인원 조회
    @GetMapping("/{postId}/count")
    @Operation(summary = "수락된 인원 조회", description = "버킷리스트 작성자가 수락된 참가신청서 수를 조회합니다.")
    public Response applicationAcceptCount(@Parameter(name = "postId", description = "게시글 id", in = ParameterIn.PATH) @PathVariable Long postId) {
        return Response.success(applicationService.applicationAcceptCount(postId));
    }
}
