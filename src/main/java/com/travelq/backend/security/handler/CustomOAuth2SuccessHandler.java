package com.travelq.backend.security.handler;

import com.travelq.backend.util.JWTUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private final JWTUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        String script;  // 프론트로 전달 될 script 코드

        // OAuth2 로그인 정보를 가져옴
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Collection<? extends GrantedAuthority> authorities = oAuth2User.getAuthorities();

        if (authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_GUEST"))) {
            // 서비스 미가입자이므로 추가 정보 입력 필드로 리다이렉트
            log.info("미등록 사용자! 추가 정보 입력 필요");

            // 토큰 생성
            /*회원가입 전에는 Redux에 저장. 추가 정보 입력 페이지에서 다른 페이지에서 이동할 땐 새로고침이 되도록하여 Redux 초기화하여 토큰 탈취 가능성을 줄임
            * 회원가입이 완료되면 Redux에 저장된 토큰을 LocalStorage에 저장하여 요청시마다 서버로 전송 가능하도록 함.
            * 현재 가입으로 얻을 수 있는 권한은 USER 권한뿐이므로 권한은 "ROLE_USER" 고정 */
            Map<String, Object> claims = Map.of(
                    "email", oAuth2User.getAttribute("email"),
                    "name", oAuth2User.getAttribute("name"),
                    "role", List.of("ROLE_USER"));

            log.info("생성된 Claim 정보 : {}", claims);

            String token = jwtUtil.generateToken(claims);

            script = "<script>" +
                    "window.opener.postMessage({ type: 'NEED_MORE_INFO', token: '" + token + "'}, 'http://localhost:3000');" +
                    "window.close();" +
                    "</script>";
        } else {
            log.info("등록된 사용자! 로그인 성공");

            // 토큰 생성
            Map<String, Object> claims = Map.of(
                    "email", oAuth2User.getAttribute("email"),
                    "name", oAuth2User.getAttribute("name"),
                    "role", oAuth2User.getAuthorities());

            log.info("생성된 Claim 정보 : {}", claims);

            String token = jwtUtil.generateToken(claims);

            log.info("토큰 생성 완료 : {}", token);

            // 서비스 가입자이므로 팝업창 종료 Script
            script = "<script>" +
                    "window.opener.postMessage({ type: 'LOGIN_SUCCESS', token: '" + token + "'}, 'http://localhost:3000');" +
                    "window.close();" +
                    "</script>";
        }

        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write(script);
        response.getWriter().flush();
    }
}