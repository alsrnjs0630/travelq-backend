package com.travelq.backend.dto.RecommendCmt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RcmCmtResponseDTO {
    // 추천 게시글 댓글 ID
    private Long id;
    // 추천 게시글 ID
    private Long recommendId;
    // 회원 아이디
    private Long memberId;
    // 작성자
    private String author;
    // 내용
    private String content;
    // 신고 수
    private Integer reportCount;
    // 상태
    private String state;
    // 등록일
    private LocalDateTime createdAt;
    // 수정일
    private LocalDateTime updatedAt;
}
