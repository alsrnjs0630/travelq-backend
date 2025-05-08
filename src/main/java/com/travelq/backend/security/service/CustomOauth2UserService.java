package com.travelq.backend.security.service;

import com.travelq.backend.entity.Member;
import com.travelq.backend.repository.MemberRepository;
import com.travelq.backend.security.dto.GoogleUserInfoDTO;
import com.travelq.backend.security.dto.KakaoUserInfoDTO;
import com.travelq.backend.security.dto.NaverUserInfoDTO;
import com.travelq.backend.security.dto.OAuth2UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CustomOauth2UserService extends DefaultOAuth2UserService {
    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        Map<String, Object> attributes = super.loadUser(userRequest).getAttributes();

        String provider = userRequest.getClientRegistration().getRegistrationId(); // 소셜 로그인 종류
        OAuth2UserInfo userInfo;

        if (provider.equals("google")) {
            userInfo = new GoogleUserInfoDTO(attributes);
        } else if (provider.equals("kakao")) {
            userInfo = new KakaoUserInfoDTO(attributes);
        } else if (provider.equals("naver")) {
            userInfo = new NaverUserInfoDTO(attributes);
        } else {
            throw new OAuth2AuthenticationException("지원하지 않는 로그인입니다.");
        }

        log.info("유저 OAuth2 로그인 정보 : {}", userInfo);

        // 인증객체에 보낼 데이터 매핑
        Map<String, Object> customAttributes = Map.of(
                "provider", userInfo.getProvider(),
                "providerId", userInfo.getProviderId(),
                "name", userInfo.getName(),
                "email", userInfo.getEmail()
        );

        // 권한 설정 (로그인한 이메일로 DB 조회 후 권한 설정)
        Optional<Member> member = memberRepository.findByEmail(userInfo.getEmail());

        List<SimpleGrantedAuthority> authorities = member
                .map(value -> value.getMemberRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getRole()))
                        .collect(Collectors.toList())
                )
                .orElseGet(() -> Collections.singletonList(
                        new SimpleGrantedAuthority("ROLE_GUEST")
                ));

        log.info("인증 객체 권한: {}", authorities);
        log.info("인증 객체 정보: {}", customAttributes);

        return new DefaultOAuth2User(authorities, customAttributes, "email");
    }
}
