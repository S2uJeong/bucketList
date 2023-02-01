package com.team9.bucket_list.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.team9.bucket_list.domain.dto.member.MemberCheckUserNameRequest;
import com.team9.bucket_list.domain.dto.member.MemberDto;
import com.team9.bucket_list.domain.dto.member.MemberJoinRequest;
import com.team9.bucket_list.domain.dto.member.MemberLoginRequest;
import com.team9.bucket_list.domain.entity.Member;
import com.team9.bucket_list.domain.entity.RefreshToken;
import com.team9.bucket_list.execption.ApplicationException;
import com.team9.bucket_list.execption.ErrorCode;
import com.team9.bucket_list.repository.MemberRepository;
import com.team9.bucket_list.repository.RefreshTokenRepository;
import com.team9.bucket_list.domain.dto.token.TokenDto;
import com.team9.bucket_list.security.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder encoder;
    private final JwtUtil jwtUtil;

    public Boolean checkUserName(MemberCheckUserNameRequest request) {
        String userName = request.getUserName();
        Optional<Member> findMember = memberRepository.findByUserName(userName);
        if (findMember.isPresent()) {
            throw new ApplicationException(ErrorCode.DUPLICATED_USERNAME);
        } else {
            return true;
        }
    }

    public Boolean checkEmail(String email) {
        Optional<Member> findMember = memberRepository.findByEmail(email);
        if (findMember.isPresent()) {
            throw new ApplicationException(ErrorCode.DUPLICATED_EMAIL);
        } else {
            return true;
        }
    }

    public MemberDto findByEmail(String email) {
        return memberRepository.findByEmail(email).orElseThrow(() -> {
            throw new ApplicationException(ErrorCode.DUPLICATED_EMAIL);
        }).toDto();
    }

    @Transactional
    public MemberDto join(MemberJoinRequest request) {
        //회원가입 요청을 받을 때 이메일 인증을 했는지 안했는지 여부를 받아야하지 않을까..?

        String email = request.getEmail();
        String password = request.getPassword();
        String passwordCorrect = request.getPasswordCorrect();

        //가입 가능 여부 체크
        memberRepository.findByEmail(email).ifPresent(member -> {
            throw new ApplicationException(ErrorCode.DUPLICATED_EMAIL);
        });

        //비밀번호 일치 여부 체크
        if (!password.equals(passwordCorrect)) {
            throw new ApplicationException(ErrorCode.INCORRECT_PASSWORD_CORRECT);
        }

        //정상 회원 저장 로직
        Member member = request.toEntity(encoder.encode(request.getPassword()));
        Member savedMember = memberRepository.save(member);
        return savedMember.toDto();
    }

    @Transactional
    public TokenDto login(MemberLoginRequest request) {
        String email = request.getEmail();
        String password = request.getPassword();

        //email이 DB에 존재하는지
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> {
            throw new ApplicationException(ErrorCode.USERNAME_NOT_FOUNDED);
        });

        //email-password 일치여부 확인
        if (!encoder.matches(password, member.getPassword())) {
            throw new ApplicationException(ErrorCode.INVALID_PASSWORD);
        }

        //정상 로그인 실행 - 토큰 발급
        TokenDto token = jwtUtil.createToken(member.getId(), member.getUserName(), member.getMemberRole());

        //Refresh Token 업데이트
        if (refreshTokenRepository.findByMemberId(member.getId()).isEmpty()) {
            RefreshToken refreshToken = RefreshToken.builder()
                    .memberId(member.getId())
                    .token(token.getRefreshToken())
                    .build();

            refreshTokenRepository.save(refreshToken);
        } else {
            RefreshToken refreshToken = refreshTokenRepository.findByMemberId(member.getId()).get();
            refreshToken.updateToken(token.getRefreshToken());
        }

        return token;
    }

    @Transactional
    public TokenDto reissue(HttpServletRequest request) throws JsonProcessingException {

        final String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String token = authorizationHeader.replace("Bearer ", "");

        Long memberId = JwtUtil.getMemberId(token);
        Member member = memberRepository.findById(memberId).orElseThrow(() -> {
            throw new ApplicationException(ErrorCode.USERNAME_NOT_FOUNDED);
        });

        //리프레시 토큰 확인
        RefreshToken refreshToken = refreshTokenRepository.findByMemberId(memberId).orElseThrow(() -> {
            throw new ApplicationException(ErrorCode.INVALID_TOKEN);
        });

        boolean isTokenValid = jwtUtil.validateToken(refreshToken.getToken());
        if (isTokenValid) {
            //토큰 재발급
            return jwtUtil.createToken(member.getId(), member.getUserName(), member.getMemberRole());
        } else {
            // 로그인 요청
            throw new ApplicationException(ErrorCode.INVALID_REFRESH_TOKEN);
        }
    }
}
