package com.travelq.backend.dto.RecommendCmt;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RcmCmtRequestDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    // 추천 게시글 ID
    private Long recommendId;
    // 댓글 내용
    @NotBlank(message = "댓글 내용을 입력해주세요.")
    private String content;
}
