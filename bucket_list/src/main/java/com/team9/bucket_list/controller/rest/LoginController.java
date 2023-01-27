package com.team9.bucket_list.controller.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.team9.bucket_list.domain.Response;
import com.team9.bucket_list.domain.dto.member.LoginResponse;
import com.team9.bucket_list.domain.dto.member.MemberLoginRequest;
import com.team9.bucket_list.domain.dto.token.TokenDto;
import com.team9.bucket_list.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginController {

    private final MemberService memberService;

    //== 로그인 요청 ==//
    @PostMapping
    public Response<LoginResponse> login(@RequestBody MemberLoginRequest memberLoginRequest) throws IOException {
        log.info("로그인 요청 IN ={}", memberLoginRequest.getEmail());
        TokenDto token = memberService.login(memberLoginRequest);
        String accessToken = token.getAccessToken();
        return Response.success(new LoginResponse(accessToken));
    }

    //== 재발급 요청 ==//
    @PostMapping("/reissue")
    public String reissue(HttpServletRequest request) throws JsonProcessingException {
        log.info("재발급 요청 토큰 = {}", request.getHeader("Authorization"));
        TokenDto reissue = memberService.reissue(request);
        return reissue.getAccessToken();
    }
}
