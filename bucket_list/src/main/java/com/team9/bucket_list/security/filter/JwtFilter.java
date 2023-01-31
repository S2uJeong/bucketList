package com.team9.bucket_list.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team9.bucket_list.domain.enumerate.MemberRole;
import com.team9.bucket_list.execption.ErrorCode;
import com.team9.bucket_list.domain.dto.token.ResponseVO;
import com.team9.bucket_list.security.utils.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    Long memberId = 0L;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String path = request.getServletPath();

            if (path.startsWith("/reissue")) {
                filterChain.doFilter(request, response);
            } else {
                //헤더에서 토큰 꺼내기
                final String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

                //헤더 형식 확인
                if (authorizationHeader == null) {
                    log.info("header가 없는 요청입니다.");
                    SecurityContextHolder.getContext().setAuthentication(null);
                    filterChain.doFilter(request, response);
                    return;
                }

                //token 분리
                String token = "";
                if (authorizationHeader.startsWith("Bearer ")) {
                    token = authorizationHeader.replace("Bearer ", "");
                } else {
                    log.error("Authorization 헤더 형식이 틀립니다. : {}", authorizationHeader);
                    filterChain.doFilter(request, response);
                    return;
                }

                memberId = JwtUtil.getMemberId(token);
                boolean isTokenValid = jwtUtil.validateToken(token);

                if (isTokenValid) {
                    this.setAuthentication(request);
                }

                filterChain.doFilter(request, response);
            }
        } catch (ExpiredJwtException e) {
            log.info("엑세스 토큰이 유효하지 않습니다.");

            ResponseVO responseVO = ResponseVO.builder()
                    .status(ErrorCode.INVALID_TOKEN.getStatus().toString())
                    .message(ErrorCode.INVALID_TOKEN.getMessage())
                    .code(ErrorCode.INVALID_TOKEN.getStatus().value()).build();

            response.setStatus(ErrorCode.INVALID_TOKEN.getStatus().value());
            response.getWriter().write(new ObjectMapper().writeValueAsString(responseVO));
            response.getWriter().flush();
        }
    }

    private void setAuthentication(HttpServletRequest request) {
        //문 열어주기, Role 바인딩
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(memberId, null, List.of(new SimpleGrantedAuthority(MemberRole.USER.name())));
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}

