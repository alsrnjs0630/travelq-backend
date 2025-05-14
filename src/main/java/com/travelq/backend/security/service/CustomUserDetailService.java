package com.travelq.backend.security.service;

import com.travelq.backend.entity.Member;
import com.travelq.backend.repository.MemberRepository;
import com.travelq.backend.util.CustomUserDetail;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public CustomUserDetail loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(username)
                .orElseThrow(() -> new EntityNotFoundException(username + " 은 존재하지 않는 회원입니다."));

        // 지연로딩을 대비해 memberRole 호출
        member.getMemberRoles().size();

        return new CustomUserDetail(member);
    }
}
