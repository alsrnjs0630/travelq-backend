package com.travelq.backend.security.service;

import com.travelq.backend.entity.Member;
import com.travelq.backend.repository.MemberRepository;
import com.travelq.backend.util.CustomUserDetail;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public CustomUserDetail loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(username)
                .orElseThrow(() -> new EntityNotFoundException(username + " 은 존재하지 않는 회원입니다."));
        return new CustomUserDetail(member);
    }
}
