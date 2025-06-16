package com.travelq.backend.dto.Recommend;

import com.travelq.backend.dto.RecommendCmt.RcmCmtResponseDTO;
import com.travelq.backend.entity.RecommendCmt;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@ToString
@EqualsAndHashCode
public class RcmResponseDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    // 추천 게시글 ID
    private Long id;
    // 작성자 ID
    private Long memberId;
    // 작성자 닉네임
    private String author;
    // 게시글 제목
    private String title;
    // 게시글 내용
    private String content;
    // 좋아요 카운트
    private Integer likeCount;
    // 조회수
    private Integer viewCount;
    // 게시글 상태
    private String state;
    // 신고 카운트
    private Integer reportCount;
    // 게시글 이미지 리스트
    private List<String> images;
    // 게시글 댓글 리스트
    private List<RcmCmtResponseDTO> recommendCmts;
    // 작성일
    private LocalDateTime createdAt;
    // 수정일
    private LocalDateTime updatedAt;
}
