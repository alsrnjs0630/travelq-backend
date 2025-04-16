package com.travelq.backend.dto.Ask;

import jakarta.validation.constraints.NotBlank;
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

    // 게시글 제목
    @NotBlank(message = "제목을 입력해주세요")
    private String title;

    // 게시글 내용
    @NotBlank(message = "내용을 입력해주세요")
    private String content;
}
