package com.travelq.backend.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;

@Slf4j
@Component
public class JWTUtil {
    // 비밀키는 최소 256비트(32자) 이상이어야 합니다.
    @Value("${jwt.secret}")
    private String secretString;
    private static final long ACCESS_TOKEN_VALIDITY_SECONDS = 1000 * 60 * 30L;

    // 서명(Signature)을 위한 Base64 인코딩
    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        this.secretKey = Keys.hmacShaKeyFor(secretString.getBytes(StandardCharsets.UTF_8));
        log.info("Secret Key 초기화 완료 : {}", secretKey);
    }

    // JWT 생성 메서드
    public String generateToken(Map<String, Object> claims) {
        try {
            return Jwts.builder()
                    .header()
                    .add("type", "JWT")
                    .add("alg", "HS256")
                    .and()
                    .subject((String) claims.get("email"))
                    .claims(claims)
                    .issuedAt(Date.from(ZonedDateTime.now().toInstant()))
                    .expiration(Date.from(ZonedDateTime.now().plusMinutes(ACCESS_TOKEN_VALIDITY_SECONDS).toInstant()))
                    .signWith(secretKey, Jwts.SIG.HS256)
                    .compact();
        } catch (Exception e) {
            log.error("JWT 생성 중 오류: {}", e.getMessage());
            throw new JwtException("JWT 토큰 발행 오류");
        }
    }

    // 토큰에서 Claims 추출
    public Claims getClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getBody();
        } catch (JwtException e) {
            log.error("Claims 추출 중 오류: {}",e.getMessage());
            throw new JwtException("토큰에서 Claims 추출 중 오류");
        }
    }

    // 토큰에서 이메일 추출
    public String getEmail(String token) {
        try {
            return getClaims(token).getSubject();
        } catch (JwtException e) {
            log.error("JWT 이메일 추출 중 오류: {}",e.getMessage());
            throw new JwtException("JWT 사용자 이메일 추출 중 오류");
        }
    }

    // 토큰 유효성 검사
    public boolean isExpired(String token) {
        try {
            Date expiration = getClaims(token).getExpiration();
            return expiration.before(new Date());
        } catch (JwtException e) {
            log.error(e.getMessage());
            // 유효성 에러도 토큰 만료로 취급
            return true;
        }
    }
}
