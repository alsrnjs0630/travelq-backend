package com.travelq.backend.repository;

import com.travelq.backend.entity.Member;
import com.travelq.backend.entity.PostLike;
import com.travelq.backend.entity.Recommend;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
@Slf4j
public class PostLikeRepositoryTest {
    @Autowired
    private PostLikeRepository postLikeRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private RecommendRepository recommendRepository;

    @Test
    public void togglePostLike() {
        log.info("--------------------좋아요 토글--------------------");

        // 로그인한 회원 찾기
        Member member = memberRepository.findById(21L)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));

        // 게시물 찾기
        Recommend recommend = recommendRepository.findById(6L)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 게시물입니다."));

        // 해당 게시물의 좋아요 목록 가져오기
        Optional<PostLike> postLike = postLikeRepository.findByRecommendAndMember(recommend, member);

        if (postLike.isPresent()) {
            // 좋아요를 이미 눌렀다면 취소 (삭제)
            log.info("좋아요 취소");
            recommend.updateLikeCount(recommend.getLikeCount() - 1);
            recommendRepository.save(recommend);
            postLikeRepository.delete(postLike.get());
        } else {
            // 좋아요를 안 눌렀다면 추가
            log.info("좋아요 추가");
            PostLike newLike = PostLike.builder()
                    .member(member)
                    .recommend(recommend)
                    .build();
            recommend.updateLikeCount(recommend.getLikeCount() + 1);
            recommendRepository.save(recommend);
            postLikeRepository.save(newLike);
        }
    }

}
