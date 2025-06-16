package com.travelq.backend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travelq.backend.dto.common.ApiResponseDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        // 응답 상태 코드 (토큰 만료 : 401 인증실패)
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json; charset=utf-8");

        // 응답 메세지 설정
        String responseMessage = new ObjectMapper().writeValueAsString(
                new ApiResponseDTO<>(false, "토큰이 만료되었습니다. 로그아웃 후 다시 로그인해주세요.", null)
        );

        // 응답 전송
        response.getWriter().write(responseMessage);
    }
}
