package com.team9.bucket_list.security.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.team9.bucket_list.domain.enumerate.MemberRole;
import com.team9.bucket_list.execption.ApplicationException;
import com.team9.bucket_list.execption.ErrorCode;
import com.team9.bucket_list.security.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ChatPreHandler implements ChannelInterceptor {

    private final JwtUtil jwtUtil;
    Long memberId = 0L;
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        try {
            log.info(message.toString());
            StompHeaderAccessor headerAccessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

            String authorizationHeader = String.valueOf(headerAccessor.getNativeHeader("Authorization"));

            log.info("jwt : " + authorizationHeader);

            if (authorizationHeader == null) {
                log.info("chat header가 없는 요청입니다.");
                throw new ApplicationException(ErrorCode.INVALID_TOKEN);
            }

            //token 분리
            String token = "";
            String authorizationHeaderStr = authorizationHeader.replace("[","").replace("]","");
            if (authorizationHeaderStr.startsWith("Bearer ")) {
                token = authorizationHeaderStr.replace("Bearer ", "");
            } else {
                log.error("Authorization 헤더 형식이 틀립니다. : {}", authorizationHeader);
                throw new ApplicationException(ErrorCode.INVALID_TOKEN);
            }

            try {
                memberId = JwtUtil.getMemberId(token);
            } catch (JsonProcessingException e) {
                throw new ApplicationException(ErrorCode.INVALID_TOKEN);
            }

            boolean isTokenValid = jwtUtil.validateToken(token);

            if (isTokenValid) {
                this.setAuthentication(message, headerAccessor);
            }
        } catch (Exception e) {
            log.error("JWT에러");
            throw new ApplicationException(ErrorCode.INVALID_TOKEN);
        }
        return message;
    }

    private void setAuthentication(Message<?> message, StompHeaderAccessor headerAccessor) {
        //문 열어주기, Role 바인딩
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(memberId, null, List.of(new SimpleGrantedAuthority(MemberRole.USER.name())));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        headerAccessor.setUser(authentication);
        log.info("채팅 인증 id받아오기 : "+authentication.getName());
    }
}
