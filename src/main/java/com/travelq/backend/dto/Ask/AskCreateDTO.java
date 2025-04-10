package com.travelq.backend.dto.Ask;

import com.travelq.backend.entity.Member;
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

    // 회원 아이디
    private Member member;

    // 게시글 제목
    private String title;

    // 게시글 내용
    private String content;

}
