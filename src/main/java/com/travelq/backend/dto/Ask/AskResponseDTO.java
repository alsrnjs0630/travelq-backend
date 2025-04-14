package com.travelq.backend.dto.Ask;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.travelq.backend.entity.Member;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@EqualsAndHashCode
public class AskResponseDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    // 질문 게시글 상세페이지 출력 DTO

    // 게시글 아이디
    private Long id;

    // 회원 아이디
    private Long memberId;

    // 게시글 제목
    private String title;

    // 작성자
    private String author;

    // 내용
    private String content;

    // 조회수
    private int viewCount;

    // 작성일
    private LocalDateTime createdAt;

    // 수정일
    private LocalDateTime updatedAt;
}
