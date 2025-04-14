package com.travelq.backend.dto.Ask;

import lombok.*;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class AskUpdateDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    // 질문 게시글 수정 DTO
    // 수정 가능 필드 : 제목, 내용

    // 게시글 ID
    private Long id;

    // 게시글 제목
    private String title;

    // 게시글 내용
    private String content;
}
