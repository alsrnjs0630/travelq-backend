package com.travelq.backend.repository;

import com.travelq.backend.entity.Member;
import com.travelq.backend.entity.Recommend;
import com.travelq.backend.entity.RecommendCmt;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class RecommendCmtRepositoryTest {
    @Autowired
    private RecommendCmtRepository recommendCmtRepository;

    @Autowired
    private RecommendRepository recommendRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void insertRecommendCmt() {
        log.info("---------------------추천 게시물 댓글 작성--------------------");

        // 로그인
        Member member = memberRepository.findById(21L)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));

        // 추천 게시판 게시물 조회
        Recommend recommend = recommendRepository.findById(6L)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 게시물입니다."));

        // 게시물 댓글 작성
        RecommendCmt recommendCmt = RecommendCmt.builder()
                .recommend(recommend)
                .memberId(member.getId())
                .author("게스트123")       // 실제 코드에선 로그인한 사용자의 정보를 가져와서 nickName이 들어감
                .content("상하이 정말 깨끗한가요? 한 번 가보고싶네요!")
                .reportCount(0)
                .state("00")
                .build();
        recommendCmtRepository.save(recommendCmt);
    }

    @Test
    public void updateRecommendCmt() {
        log.info("---------------------추천 게시물 댓글 수정--------------------");

        // 로그인
        Member member = memberRepository.findById(21L)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));

        // 추천 게시판 게시물 조회
        Recommend recommend = recommendRepository.findById(6L)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 게시물입니다."));

        // 댓글 조회
        RecommendCmt recommendCmt = recommendCmtRepository.findById(2L)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 댓글입니다."));

        // 댓글 작성자와 로그인한 회원의 아이디가 같은지 확인
        if (!member.getId().equals(recommendCmt.getMemberId())) {
            log.info("수정 불가능한 댓글입니다.");
            return; // 더 이상 진행하지 않도록 종료
        }

        try {
            recommendCmt.updateContent("상하이보단 도쿄지~");
            recommendCmt.updateReportCount(1);
            recommendCmtRepository.save(recommendCmt);
            log.info("댓글이 성공적으로 수정되었습니다.");
        } catch (Exception e) {
            log.error("댓글 수정 중 오류 발생: {}", e.getMessage(), e);
        }
    }

}
