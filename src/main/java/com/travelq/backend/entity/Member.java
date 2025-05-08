package com.travelq.backend.entity;

import com.travelq.backend.util.commonCode.StatusCode;
import com.travelq.backend.util.commonCode.StatusConverter;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Member {

    // 회원 아이디
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 소셜 로그인 제공자 (예: google, facebook 등)
    @Column(name = "social_provider", nullable = true)
    private String socialProvider;

    // 소셜 로그인 고유 ID
    @Column(name = "social_id", nullable = true, unique = true)
    private String socialId;

    // 이름
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    // 생년월일
    @Column(name = "birthday", nullable = false, length = 8)
    private String birthday;

    // 닉네임
    @Column(name = "nickName", nullable = false)
    private String nickName;

    // 주소
    @Column(name = "address")
    private String address;

    // 이메일
    @Column(name = "email", nullable = false, length = 255, unique = true)
    private String email;

    // 회원상태 (00 = 정상, 01 = 삭제(탈퇴), 02 = 신고(정지))
    @Convert(converter = StatusConverter.class)
    @Column(name = "member_state", nullable = false, length = 2)
    private StatusCode memberState;

    // 신고 수
    @Column(name = "report_count", nullable = false)
    private int reportCount;

    // 등록일
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // 수정일
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // 회원 권한
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<MemberRole> memberRoles;

    // 좋아요
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PostLike> likes;

    // 추천 게시판
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Recommend> recommends;

    // 질문 게시판
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Ask> asks;

    // 닉네임 수정
    public void updateNickName(String nickName) {
        this.nickName = nickName;
    }

    // 회원 상태 변경
    public void updateMemberState(StatusCode memberState) {
        this.memberState = memberState;
    }

    // 권한 등록
    public void addRole(MemberRole memberRole) {
        memberRoles.add(memberRole);
        memberRole.setMember(this);
    }
}
