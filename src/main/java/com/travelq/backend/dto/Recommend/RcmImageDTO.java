package com.travelq.backend.dto.Recommend;

import lombok.*;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class RcmImageDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    // 부모 게시글 아이디 (추천 게시글)
    private Long recommendId;

    // 이미지명
    private String iamgeName;

    // 삭제 여부
    private Boolean isDeleted;
}
