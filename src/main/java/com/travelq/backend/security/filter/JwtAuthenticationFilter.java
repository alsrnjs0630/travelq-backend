package com.travelq.backend.security.filter;

import com.travelq.backend.security.service.CustomUserDetailService;
import com.travelq.backend.util.CustomUserDetail;
import com.travelq.backend.util.JWTUtil;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JWTUtil jwtUtil;
    private final CustomUserDetailService customUserDetailService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        // "Bearer" 제거
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try {
                // 요청 헤더에서 토큰 추출
                String token = authHeader.substring(7);
                if(!jwtUtil.isExpired(token)) {
                    // 토큰이 유효한 경우
                    String email = jwtUtil.getEmail(token);
                    if(email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                        // 유저 조회
                        CustomUserDetail userDetails = customUserDetailService.loadUserByUsername(email);
                        log.info("토큰 파싱 이메일 : {}", userDetails.getUsername());
                        log.info("토큰 파싱 유저 이름 : {}", userDetails.getName());
                        log.info("토큰 파싱 유저 권한 : {}", userDetails.getAuthorities());
                        // 인증 객체 생성
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        // 인증 객체 저장
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        log.info("JWT 인증 성공");
                    }
                } else {
                    throw new JwtException("토큰이 만료되었습니다.");
                }
            } catch (Exception e) {
                // JWT 검증 실패 처리
                log.error("인증되지 않은 토큰입니다 : ", e);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }
        // 다음 필터로 넘김
        filterChain.doFilter(request, response);
    }
}
