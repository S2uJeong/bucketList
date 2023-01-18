package com.team9.bucket_list.service;

import com.team9.bucket_list.domain.dto.Member.MemberDto;
import com.team9.bucket_list.domain.dto.Member.MemberJoinRequest;
import com.team9.bucket_list.domain.entity.Member;
import com.team9.bucket_list.execption.ApplicationException;
import com.team9.bucket_list.execption.ErrorCode;
import com.team9.bucket_list.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public MemberDto join(MemberJoinRequest memberJoinRequest) {
        //회원가입 요청을 받을 때 이메일 인증을 했는지 안했는지 여부를 받아야하지 않을까..?

        //가입 가능 여부 체크
        String email = memberJoinRequest.getEmail();
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        optionalMember.ifPresent(member -> {
            throw new ApplicationException(ErrorCode.DUPLICATED_EMAIL);
        });

        //비밀번호 일치 여부 체크
        String password = memberJoinRequest.getPassword();
        String passwordCorrect = memberJoinRequest.getPasswordCorrect();
        if (!password.equals(passwordCorrect)) {
            throw new ApplicationException(ErrorCode.INCORRECT_PASSWORD_CORRECT);
        }

        //정상 회원 저장 로직
        Member member = memberJoinRequest.toEntity();
        log.info("member={}", member.toString());
        Member savedMember = memberRepository.save(member);
        return savedMember.toDto();
    }
}
