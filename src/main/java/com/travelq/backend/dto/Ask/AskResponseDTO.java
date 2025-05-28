package com.travelq.backend.dto.Ask;

import com.travelq.backend.dto.AskCmt.AskCmtResponseDTO;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder(toBuilder = true)
@EqualsAndHashCode
public class AskResponseDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    // 질문 게시글 상세페이지 출력 DTO

    // 게시글 아이디
    private Long id;

    // 게시글 제목
    private String title;

    // 작성자
    private String author;

    // 내용
    private String content;

    // 조회수
    private int viewCount;

    // 신고수
    private int reportCount;

    // 게시글 상태
    private String state;

    // 게시글 댓글 목록
    private List<AskCmtResponseDTO> cmts;

    // 작성일
    private LocalDateTime createdAt;

    // 수정일
    private LocalDateTime updatedAt;
}
