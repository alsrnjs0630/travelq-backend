package com.travelq.backend.dto.AskCmt;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class AskCmtRequestDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    // 게시판 아이디
    private Long askId;

    // 댓글 내용
    @NotBlank(message = "내용을 입력해주세요")
    private String content;
}
