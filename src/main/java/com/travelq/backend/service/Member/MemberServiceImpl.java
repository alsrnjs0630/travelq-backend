package com.travelq.backend.service.Member;

import com.travelq.backend.dto.Member.MemberCreateDTO;
import com.travelq.backend.dto.common.ApiResponseDTO;
import com.travelq.backend.entity.Member;
import com.travelq.backend.entity.MemberRole;
import com.travelq.backend.repository.MemberRepository;
import com.travelq.backend.repository.MemberRoleRepository;
import com.travelq.backend.util.commonCode.StatusCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final MemberRoleRepository memberRoleRepository;

    // 회원 등록
    @Override
    public ResponseEntity<ApiResponseDTO<MemberCreateDTO>> createMember(MemberCreateDTO memberCreateDTO) {
        // 현재 임시 인증된 OAuth2 객체 로드
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        log.info("회원등록 시작 --");
        log.info("Authentication 정보: {}", authentication);

        // OAuth2로 로그인한 인증 객체가 null 이거나 인증되지 않은 경우
        if (authentication == null || !authentication.isAuthenticated()) {
            log.info("인증에러 1");
            throw new IllegalStateException("인증된 사용자가 아닙니다.");
        }

        Object principal = authentication.getPrincipal();

        log.info("인증객체 타입 : {}", principal);

        if (principal instanceof DefaultOAuth2User oAuth2User) {
            log.info("인증객체 로드 성공 DB 저장 시작");
            // 회원 등록
            MemberRole memberRole = new MemberRole();
            Member member = Member.builder()
                    .name(memberCreateDTO.getName())
                    .email(memberCreateDTO.getEmail())
                    .nickName(memberCreateDTO.getNickname())
                    .birthday(memberCreateDTO.getBirthday())
                    .address(memberCreateDTO.getAddress())
                    .socialId(oAuth2User.getAttribute("providerId"))
                    .socialProvider(oAuth2User.getAttribute("provider"))
                    .reportCount(0)
                    .memberState(StatusCode.NORMAL)
                    .memberRoles(new ArrayList<>())
                    .build();

            memberRepository.save(member);

            // 회원 권한 설정 (Default : ROLE_USER)
            memberRole.setMember(member);
            memberRole.setRole("ROLE_USER");
            memberRoleRepository.save(memberRole);

            member.addRole(memberRole);

            // Authentication 객체 교체-----
            // 새 권한 생성
            List<GrantedAuthority> newAuthorities = member.getMemberRoles().stream()
                    .map(role -> new SimpleGrantedAuthority(role.getRole()))
                    .collect(Collectors.toList());

            // 새로운 OAuth2 객체 생성
            DefaultOAuth2User newOAuth2User = new DefaultOAuth2User(newAuthorities, oAuth2User.getAttributes(), "email");

            //새로운 Authentication 객체 생성
            String registrationId = newOAuth2User.getAttribute("provider");
            OAuth2AuthenticationToken newAuth = new OAuth2AuthenticationToken(newOAuth2User, newAuthorities, registrationId);

            // SecurityContext에 저장
            SecurityContextHolder.getContext().setAuthentication(newAuth);

            log.info("새로운 인증 객체 생성!!!!! : {}", newAuth);

            // 응답 생성
            ApiResponseDTO<MemberCreateDTO> createResponse = ApiResponseDTO.<MemberCreateDTO>builder()
                    .success(true)
                    .message("회원등록 성공-----")
                    .data(memberCreateDTO)
                    .build();

            return ResponseEntity.status(HttpStatus.CREATED).body(createResponse);
        } else {
            log.info("인증에러 2");
            throw new IllegalStateException("인증된 사용자가 아닙니다.");
        }
    }
}
