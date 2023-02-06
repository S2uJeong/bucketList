package com.team9.bucket_list.controller.rest;

import com.team9.bucket_list.domain.Response;
import com.team9.bucket_list.domain.dto.member.MemberCheckUserNameRequest;
import com.team9.bucket_list.domain.dto.member.MemberDto;
import com.team9.bucket_list.domain.dto.member.MemberJoinRequest;
import com.team9.bucket_list.execption.ApplicationException;
import com.team9.bucket_list.execption.ErrorCode;
import com.team9.bucket_list.service.MailService;
import com.team9.bucket_list.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/join")
@Tag(name = "회원가입", description = "회원가입을 합니다.")
public class JoinController {

    private final MemberService memberService;
    private final MailService mailService;

    @PostMapping("/checkUserName")
    @Operation(summary = "닉네임 중복 검사", description = "UserName이 중복되는지 확인합니다.")
    public Response<Boolean> checkUserName(@RequestBody MemberCheckUserNameRequest request) {
        log.info("닉네임 중복 검사 요청 = {}", request.getUserName());
        Boolean checkUserName = memberService.checkUserName(request);
        return Response.success(checkUserName);
    }
    //== 회원가입 요청 ==//
    @PostMapping
    @Operation(summary = "회원가입 요청", description = "회원가입을 합니다.")
    public Response<String> join(@RequestBody MemberJoinRequest memberJoinRequest) {
        log.info("회원가입 요청 = {}", memberJoinRequest.toString());
        MemberDto joinMemberDto = memberService.join(memberJoinRequest);
        return Response.success("회원가입 성공");
    }

    //== 이메일 인증코드 전송 요청 ==//
    @PostMapping("/email")
    @Operation(summary = "이메일 인증코드 전송 요청", description = "회원가입 시, 입력한 이메일로 보내진 인증코드를 기입해야 가입 가능합니다.")
    public Response<String> sendCode(@RequestBody Map<String, String> emailMap) throws Exception {
        String email = emailMap.get("email");
        log.info("email={}", email);
        if (memberService.checkEmail(email)) {
            String code = mailService.sendSimpleMessage(email);
            return Response.success(code);
        } else {
            throw new ApplicationException(ErrorCode.DUPLICATED_EMAIL);
        }
    }
}
