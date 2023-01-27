package com.team9.bucket_list.security.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team9.bucket_list.domain.enumerate.MemberRole;
import com.team9.bucket_list.domain.dto.token.TokenDto;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;
import java.util.Map;

@Slf4j
@Component
public class JwtUtil {
    @Value("${jwt.secret.key}")
    private String secretKey;

    private final long accessTokenValidTime = 1000L * 5;
    private final long refreshTokenValidTime = accessTokenValidTime * 10;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    /**
     * Claims 추출 메서드
     */
    private static Claims extractClaims(String token, String secretkey) {
        return Jwts.parser().setSigningKey(secretkey).parseClaimsJws(token).getBody();
    }

    /**
     * 토큰에서 member_id 추출
     */
    public static Long getMemberId(String token) throws JsonProcessingException {
        String payload = token.split("\\.")[1];

        Base64.Decoder decoder = Base64.getDecoder();
        payload = new String(decoder.decode(payload.getBytes()));

        ObjectMapper mapper = new ObjectMapper();
        Map map = mapper.readValue(payload, Map.class);

        return Long.parseLong(map.get("memberId").toString());
    }

    // 토큰의 유효 및 만료 확인
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch(SecurityException | MalformedJwtException e) {
            log.error("Invalid JWT signature");
            return false;
        } catch(UnsupportedJwtException e) {
            log.error("Unsupported JWT token");
            return false;
        } catch(IllegalArgumentException e) {
            log.error("JWT token is invalid");
            return false;
        }
    }

    /**
     * 토큰 생성
     */
    public TokenDto createToken(Long memberPk, MemberRole role) {
        Claims claims = Jwts.claims();
        claims.put("memberId", memberPk);
        claims.put("role", role);

        String accessToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenValidTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenValidTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        return TokenDto.builder()
                .grantType("bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenExpireDate(accessTokenValidTime)
                .build();

    }
}
