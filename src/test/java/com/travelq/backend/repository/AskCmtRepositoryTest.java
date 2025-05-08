package com.travelq.backend.repository;

import com.travelq.backend.entity.Ask;
import com.travelq.backend.entity.AskCmt;
import com.travelq.backend.entity.Member;
import com.travelq.backend.util.commonCode.StatusCode;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class AskCmtRepositoryTest {
    @Autowired
    private AskCmtRepository askCmtRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AskRepository askRepository;

    @Test
    public void insertAskCmt() {
        log.info("-------------------질문 게시판 댓글 등록---------------------");
        // 로그인 및 게시글 선택
        Member member = memberRepository.findById(21L)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));

        Ask ask = askRepository.findById(12L)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 게시물입니다."));

        // 댓글 작성
        AskCmt askCmt = AskCmt.builder()
                .ask(ask)
                .memberId(member.getId())
                .author(member.getNickName())
                .content("일본 날씨 괜찮나요!?")
                .reportCount(0)
                .state(StatusCode.NORMAL)
                .build();

        askCmtRepository.save(askCmt);
    }

    @Test
    public void updateAskCmt() {
        log.info("-------------------질문 게시판 댓글 수정---------------------");
        // 로그인 및 게시글 선택
        Member member = memberRepository.findById(21L)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));

        Ask ask = askRepository.findById(12L)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 게시물입니다."));

        // 수정할 댓글
        AskCmt askCmt = askCmtRepository.findById(1L)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 댓글입니다."));

        try {
            askCmt.updateContent("네 ㅜㅜ 지금 후쿠오카는 좀 더워요");
            askCmt.updateReportCount(1);
            askCmtRepository.save(askCmt);
        }catch (Exception e){
            log.error("댓글 수정중 에러 발생! 다시 시도해 주세요. 에러: {}", e.getMessage());
        }
    }

}
