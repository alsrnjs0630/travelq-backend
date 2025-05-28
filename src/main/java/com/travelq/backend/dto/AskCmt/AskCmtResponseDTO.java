package com.travelq.backend.dto.AskCmt;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class AskCmtResponseDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    // 게시판 댓글 아이디
    private Long id;

    // 게시판
    private Long askId;

    // 작성 회원
    private Long memberId;

    // 작성자 닉네임
    private String author;

    // 댓글 내용
    private String content;

    // 신고 수
    private int reportCount;

    // 댓글 상태
    private String state;

    // 작성일
    private LocalDateTime createdAt;

    // 수정일
    private LocalDateTime updatedAt;

}
