package com.travelq.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class RecommendImg {
    // 추천 게시판 이미지 아이디
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 추천 게시판 아이디(FK)
    @ManyToOne
    @JoinColumn(name = "recommend_id", nullable = false)
    private Recommend recommend;

    // 이미지명
    @Column(name = "imageName", nullable = false)
    private String imageName;

    // 이미지 삭제 유무
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    // 등록일
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // 수정일
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
