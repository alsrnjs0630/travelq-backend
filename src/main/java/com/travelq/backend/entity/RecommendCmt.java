package com.travelq.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.travelq.backend.util.commonCode.StatusCode;
import com.travelq.backend.util.commonCode.StatusConverter;
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
public class RecommendCmt {
    // 추천 게시판 댓글 아이디
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 추천 게시판 아이디 (FK)
    @ManyToOne
    @JoinColumn(name = "recommend_id", nullable = false)
    private Recommend recommend;

    // 회원 아이디
    @Column(name = "member_id", nullable = false)
    private Long memberId;

    // 작성자
    @Column(name = "author", nullable = false)
    private String author;

    // 내용
    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    // 신고 수
    @Column(name = "report_count", nullable = false)
    private int reportCount;

    // 댓글 상태(00 = 정상, 01 = 삭제, 02 = 신고)
    @Convert(converter = StatusConverter.class)
    @Column(name = "state", nullable = false)
    private StatusCode state;

    // 등록일
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // 수정일
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // 수정 메서드
    // 내용 수정
    public void updateContent(String content) {
        this.content = content;
    }
    // 신고 수 수정
    public void updateReportCount(int reportCount) {
        this.reportCount = reportCount;
    }
    // 상태 수정
    public void updateState(StatusCode state) {
        this.state = state;
    }
}
