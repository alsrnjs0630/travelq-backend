package com.travelq.backend.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        String script;  // 프론트로 전달 될 script 코드

        // OAuth2 로그인 정보를 가져옴
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        // 권한이 'ROLE_GUEST'라면 가입되지 않은 유저이므로 추가정보 필드로 리다이렉트

        Collection<? extends GrantedAuthority> authorities = oAuth2User.getAuthorities();

        if (authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_GUEST"))) {
            // 서비스 미가입자이므로 추가 정보 입력 필드로 리다이렉트
            log.info("미등록 사용자! 추가 정보 입력 필요");

            /*script = "<script>" +
                    "window.location.href='http://localhost:3000/login/signup';" +
                    "</script>";*/

            script = "<script>" +
                    "window.opener.postMessage('NEED_MORE_INFO', 'http://localhost:3000');" +
                    "window.close();" +
                    "</script>";
        } else {
            log.info("등록된 사용자! 로그인 성공");

            // 서비스 가입자이므로 팝업창 종료 Script
            script = "<script>" +
                    "window.opener.postMessage('LOGIN_SUCCESS', 'http://localhost:3000');" +
                    "window.close();" +
                    "</script>";
        }

        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write(script);
        response.getWriter().flush();
    }
}
