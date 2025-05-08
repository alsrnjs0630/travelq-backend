package com.travelq.backend.repository;

import com.travelq.backend.entity.Member;
import com.travelq.backend.entity.MemberRole;
import com.travelq.backend.util.commonCode.StatusCode;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Slf4j
public class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberRoleRepository memberRoleRepository;

    @Test
    public void insertMember() {
        log.info("----------------------회원등록 테스트-------------------------");

        for(int i=0; i<3; i++) {
            List<MemberRole> memberRoles = new ArrayList<>();

            Member member = Member.builder()
                    .memberRoles(memberRoles)
                    .address("경기도 시흥시 배곧4로 81-2" + i)
                    .memberState(StatusCode.NORMAL)
                    .name("회원" + (i+1))
                    .birthday("19980630")
                    .email("testUser" + (i+3) + "@gamil.com")
                    .nickName("권한 테스트 어드민"+(i+1))
                    .reportCount(0)
                    .socialProvider("Google")
                    .socialId("0000000111"+i)
                    .build();

            // ROLE_USER 역할을 추가
            MemberRole memberRoleUser = new MemberRole();
            memberRoleUser.setRole("ROLE_USER");
            member.addRole(memberRoleUser);

            // ROLE_ADMIN 역할을 추가
            MemberRole memberRoleAdmin = new MemberRole();
            memberRoleAdmin.setRole("ROLE_ADMIN");
            member.addRole(memberRoleAdmin);

            memberRepository.save(member);
        }
    }

    @Test
    public void findMember() {
        log.info("----------------------회원조회 테스트-------------------------");

        Member member = memberRepository.findById(1L)
                .orElseThrow(() -> new EntityNotFoundException("회원 정보가 존재하지 않습니다."));

        log.info("회원이름: {}", member.getName());
        log.info("로그인 이용 소셜: {}", member.getSocialProvider());
        log.info("이메일: {}", member.getEmail());
    }

    @Test
    public void updateMember() {
        // 회원 탈퇴는 MemberState 변경으로 대체됩니다.
        // MemberState (00 : 정상) , (01 : 정지)
        log.info("----------------------회원정보 수정 테스트-------------------------");

        Member member = memberRepository.findById(1L)
                .orElseThrow(() -> new EntityNotFoundException("회원 정보가 존재하지 않습니다."));

        log.info("변경 전 닉네임: {}", member.getNickName());
        log.info("현재 회원 상태: {}", member.getMemberState().equals(StatusCode.NORMAL) ? "정상" : "정지");

        member.updateNickName("철수");
//        member.updateMemberState("01");
        memberRepository.save(member);

        log.info("변경 후 닉네임: {}", member.getNickName());
        log.info("현재 회원 상태: {}", member.getMemberState().equals(StatusCode.NORMAL) ? "정상" : "정지");
    }
}
