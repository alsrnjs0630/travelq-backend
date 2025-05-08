package com.travelq.backend.controller;

import com.travelq.backend.dto.Member.MemberCreateDTO;
import com.travelq.backend.dto.common.ApiResponseDTO;
import com.travelq.backend.repository.MemberRepository;
import com.travelq.backend.service.Member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class MemberController {
    private final MemberRepository memberRepository;
    private final MemberService memberService;

    // 회원 등록
    @PostMapping("/")
    public ResponseEntity<ApiResponseDTO<MemberCreateDTO>> createMember (@ModelAttribute MemberCreateDTO memberCreateDTO) {
        return memberService.createMember(memberCreateDTO);
    }
}
