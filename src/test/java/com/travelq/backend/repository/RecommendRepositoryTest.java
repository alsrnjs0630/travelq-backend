package com.travelq.backend.repository;

import com.travelq.backend.entity.Member;
import com.travelq.backend.entity.Recommend;
import com.travelq.backend.util.commonCode.StatusCode;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class RecommendRepositoryTest {
    @Autowired
    private RecommendRepository recommendRepository;
    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void insertRecommend() {
        log.info("--------------------추천게시판 등록--------------------");

        // 작성자 호출
        Member member = memberRepository.findById(21L)
                .orElseThrow(() -> new EntityNotFoundException("회원정보가 존재하지 않습니다."));

        for(int i=0; i<5; i++) {
            Recommend recommend = Recommend.builder()
                    .member(member)
                    .author(member.getNickName())
                    .title("추천 게시판 테스트글" + i)
                    .content("테스트입니다. 테테스스트트" + i)
                    .likeCount(0)
                    .reportCount(0)
                    .viewCount(0)
                    .state(StatusCode.NORMAL)
                    .build();

            recommendRepository.save(recommend);
        }
    }

    @Test
    public void findAllRecommend() {
        log.info("--------------------추천게시판 조회--------------------");

        recommendRepository.findAll().forEach(recomend -> {
            log.info("제목: {}", recomend.getTitle());
            log.info("작성자: {}", recomend.getAuthor());
            log.info("내용: {}", recomend.getContent());
        });

    }

    @Test
    public void updateRecommend() {
        log.info("--------------------추천게시판 수정--------------------");

        Recommend recommend = recommendRepository.findById(6L)
                .orElseThrow(() -> new EntityNotFoundException("없는 게시물입니다."));

        recommend.updateTitle("생각보다 깨끗한 도시, 상하이!");
        recommend.updateContent("상하이는 생각보다 깨끗했어요. 하지만 담배는 아무데서나 피더라구요");
        recommend.updateLikeCount(recommend.getLikeCount()+1);
        recommend.updateViewCount(recommend.getViewCount()+1);
        recommendRepository.save(recommend);

    }

}
