package com.travelq.backend.repository;

import com.travelq.backend.entity.Ask;
import com.travelq.backend.entity.Member;
import com.travelq.backend.util.commonCode.StatusCode;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class AskRepositoryTest {
    @Autowired
    private AskRepository askRepository;

    @Autowired
    private MemberRepository memberRepository;


    @Test
    public void insertAsk() {
        log.info("--------------------질문 게시물 등록--------------------");

        // 작성자 호출
        Member member = memberRepository.findById(21L)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));

        for(int i=0; i<5; i++) {
            Ask ask = Ask.builder()
                    .member(member)
                    .title("테스트 질문 게시글" + (i+1))
                    .author(member.getNickName())
                    .content("질문 게시글 테스트 내용입니다. ")
                    .reportCount(0)
                    .viewCount(0)
                    .state(StatusCode.NORMAL)
                    .build();

            askRepository.save(ask);
        }
    }

    @Test
    public void findAskById() {
        log.info("--------------------질문 게시물 조회--------------------");
        Ask ask = askRepository.findById(7L).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 게시물입니다."));
        // 실제 코드에서는 게시물 조회수는 유저당 하루에 한 번만 조회수가 올라가도록 구현
        ask.updateViewCount(ask.getViewCount()+1);
        askRepository.save(ask);

        log.info("게시물 조회 성공!");
        log.info("제목: {}", ask.getTitle());
        log.info("작성자: {}", ask.getAuthor());
        log.info("내용: {}", ask.getContent());
        log.info("조회수: {}", ask.getViewCount());
    }

    @Test
    public void updateAsk() {
        log.info("--------------------질문 게시물 수정--------------------");
        // 수정하고싶은 게시물 호출
        Ask ask = askRepository.findById(12L).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 게시물입니다."));

        // 게시물 수정
        ask.updateTitle("후쿠오카 지금 덥나요?");
        ask.updateContent("제목이 곧 내용입니다. 제곧내");
        ask.updateState(StatusCode.DELETED);
        askRepository.save(ask);

    }

}
