package com.team9.bucket_list.service;

import com.team9.bucket_list.domain.dto.member.MemberProfile;
import com.team9.bucket_list.domain.dto.member.OAuthAttributes;
import com.team9.bucket_list.domain.entity.Member;
import com.team9.bucket_list.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuthService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest); //OAuth 서비스(google..)에서 가져온 유저 정보
        log.info("oAuth2User : {}", oAuth2User.toString());
        Map<String, Object> attributes = oAuth2User.getAttributes();   //유저 정보 Map에 담음
        log.info("attribute : {}", attributes.toString());

        String registrationId = userRequest.getClientRegistration().getRegistrationId(); //사용한 OAuth 서비스 이름
        log.info("registrationId : {}", registrationId);

        //OAuth 서비스에 따라 유저정보를 공통된 class인 UserProfile 객체로 만들어 준다.
        MemberProfile memberProfile = OAuthAttributes.extract(registrationId, oAuth2User); /**attribute만 넘기도록 리팩토링 필요**/

        Member member = saveOrUpdate(registrationId, memberProfile);      //DB에 저장
        log.info("userName : {}", oAuth2User.getName());
        return memberProfile;
    }
    private Member saveOrUpdate(String registrationId, MemberProfile memberProfile){
        String oAuthId = (String) memberProfile.getAttributes().get("oauthId");
        Member member = null;

        switch (registrationId) {
            case "google":
                member = memberRepository.findByOauthId(oAuthId)
                        .map(m -> m.updateGoogle((String) memberProfile.getAttributes().get("email"))) //OAuth 서비스 유저정보 변경이 있으면 업데이트
                        .orElse(memberProfile.googleToMember());          //user가 없으면 새로운 user 생성
                break;

            case "naver":
                member = memberRepository.findByOauthId(oAuthId)
                        .map(m -> m.updateNaver((String) memberProfile.getAttributes().get("email"),
                                (String) memberProfile.getAttributes().get("gender"),
                                (String) memberProfile.getAttributes().get("birthYear"))) //OAuth 서비스 유저정보 변경이 있으면 업데이트
                        .orElse(memberProfile.naverToMember());          //user가 없으면 새로운 user 생성
                break;
        }

        return memberRepository.save(member);
    }
}
