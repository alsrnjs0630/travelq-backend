package com.travelq.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class PostLike {
    // 좋아요 아이디
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 회원 아이디 FK
    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    // 추천 게시물 아이디 FK
    @ManyToOne
    @JoinColumn(name = "recommend_id", nullable = false)
    private Recommend recommend;

}
