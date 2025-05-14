package com.travelq.backend.dto.Ask;

import com.travelq.backend.entity.Member;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class AskCreateDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    // 질문 게시글 등록 DTO
    // 게시글 제목
    @NotBlank(message = "제목을 입력해주세요")
    private String title;

    // 게시글 내용
    @NotBlank(message = "내용을 입력해주세요")
    private String content;

}
