package com.travelq.backend.repository;

import com.travelq.backend.entity.Member;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void insertMember() {
        log.info("----------------------회원등록 테스트-------------------------");

        for(int i=0; i<15; i++) {
            Member member = Member.builder()
                    .address("경기도 시흥시 배곧4로 81-2" + i)
                    .memberState("00")
                    .name("회원" + (i+1))
                    .birthday("19980630")
                    .email("testUser" + i + "@naver.com")
                    .nickName("테스트회원"+(i+1))
                    .reportCount(0)
                    .socialProvider("Google")
                    .socialId("0000000001"+i)
                    .build();

            memberRepository.save(member);
        }
    }

    @Test
    public void findMember() {
        log.info("----------------------회원조회 테스트-------------------------");

        Member member = memberRepository.findById(21L)
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

        Member member = memberRepository.findById(21L)
                .orElseThrow(() -> new EntityNotFoundException("회원 정보가 존재하지 않습니다."));

        log.info("변경 전 닉네임: {}", member.getNickName());
        log.info("현재 회원 상태: {}", member.getMemberState().equals("00") ? "정상" : "정지");

        member.updateNickName("철수");
//        member.updateMemberState("01");
        memberRepository.save(member);

        log.info("변경 후 닉네임: {}", member.getNickName());
        log.info("현재 회원 상태: {}", member.getMemberState().equals("00") ? "정상" : "정지");
    }
}
