package com.team9.bucket_list.controller.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.team9.bucket_list.domain.Response;
import com.team9.bucket_list.domain.dto.member.MemberLoginResponse;
import com.team9.bucket_list.domain.dto.member.MemberLoginRequest;
import com.team9.bucket_list.domain.dto.token.TokenDto;
import com.team9.bucket_list.execption.ApplicationException;
import com.team9.bucket_list.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping
@Tag(name = "로그인", description = "로그인을 합니다.")
public class LoginRestController {

    private final MemberService memberService;

    //== 로그인 요청 ==//
    @PostMapping("/api/v1/login")
    @Operation(summary = "로그인 요청", description = "email과 비밀번호를 입력해 로그인 합니다.")
    public Response<MemberLoginResponse> login(@RequestBody MemberLoginRequest memberLoginRequest,
                                               HttpServletRequest request) {

        log.info("로그인 요청 IN ={}", memberLoginRequest.getEmail());

        TokenDto token = memberService.login(memberLoginRequest);
        String accessToken = token.getAccessToken();

        if (accessToken != null) {
            HttpSession session = request.getSession(true);
            session.setAttribute("email", memberLoginRequest.getEmail());
        }

        return Response.success(new MemberLoginResponse(accessToken));
    }

    //== 재발급 요청 ==//
    @PostMapping("/api/v1/login/reissue")
    @Operation(summary = "토큰 재발급 요청", description = "토큰을 재발급합니다.")
    public String reissue(HttpServletRequest request) throws JsonProcessingException {
        log.info("재발급 요청 토큰 = {}", request.getHeader("Authorization"));
        try {
            TokenDto reissue = memberService.reissue(request);
            return reissue.getAccessToken();
        } catch (ApplicationException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
