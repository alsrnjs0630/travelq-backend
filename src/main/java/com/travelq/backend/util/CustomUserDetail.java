package com.travelq.backend.util;

import com.travelq.backend.entity.Member;
import com.travelq.backend.entity.MemberRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Slf4j
@AllArgsConstructor
public class CustomUserDetail implements UserDetails {

    @Getter
    private final String name;
    private final String email;
    private final List<MemberRole> authorities;

    public CustomUserDetail(Member member) {
        this.name = member.getName();
        this.email = member.getEmail();
        this.authorities = member.getMemberRoles();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities.stream()
                .map(role -> new SimpleGrantedAuthority(role.getRole()))
                .toList();
    }

    // 사용자는 전부 OAuth2 로그인이므로 패스워드는 입력하지 않았음. (사용하지 않는 데이터)
    @Override
    public String getPassword() {
        return "N/A";
    }

    @Override
    public String getUsername() {
        return email;
    }

    // 만료 여부
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 잠김 여부
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 비밀번호 만료 여부
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 계정 활성화 여부
    /* 신고 수에 따라 회원 상태가 '정지'상태가 됐을 때 이 함수로 정지 여부를 판단 가능(?) */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
