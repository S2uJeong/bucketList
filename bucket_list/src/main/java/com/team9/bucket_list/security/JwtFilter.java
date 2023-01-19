package com.team9.bucket_list.security;

import com.team9.bucket_list.domain.enumerate.MemberRole;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //헤더에서 토큰 꺼내기
        final String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        //헤더 형식 확인
        if(authorizationHeader == null){
            SecurityContextHolder.getContext().setAuthentication(null);
            filterChain.doFilter(request, response);
            return;
        }
        //token 분리
        String token = "";
        if (authorizationHeader.startsWith("Bearer ")){
            token = authorizationHeader.replace("Bearer ", "");
        } else{
            log.error("Authorization 헤더 형식이 틀립니다. : {}", authorizationHeader);
            filterChain.doFilter(request, response);
            return;
        }
        //token을 authentication 만들기
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken("Test", null, List.of(new SimpleGrantedAuthority(MemberRole.USER.name())));
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}

