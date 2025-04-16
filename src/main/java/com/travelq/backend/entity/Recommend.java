package com.travelq.backend.entity;

import com.travelq.backend.util.StatusCode;
import com.travelq.backend.util.StatusConverter;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Recommend {
    // 추천 게시판 아이디
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 회원 아이디
    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    // 작성자
    @Column(name = "author", nullable = false)
    private String author;

    // 제목
    @Column(name = "title", nullable = false)
    private String title;

    // 내용
    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    // 좋아요 수
    @Column(name = "like_count", nullable = false)
    private int likeCount;

    // 신고 수
    @Column(name = "report_count", nullable = false)
    private int reportCount;

    // 조회 수
    @Column(name = "view_count", nullable = false)
    private int viewCount;

    // 게시글 상태(00 = 정상, 01 = 삭제, 02 = 신고)
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

    // 좋아요
    @OneToMany(mappedBy = "recommend", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<PostLike> likes;

    // 추천 게시판 댓글
    @OneToMany(mappedBy = "recommend", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<RecommendCmt> recommendCmts;

    // 추천 게시판 이미지
    @OneToMany(mappedBy = "recommend", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<RecommendImg> recommendImgs;

    // 수정 메서드
    // 제목 수정
    public void updateTitle(String title) {
        this.title = title;
    }
    // 내용 수정
    public void updateContent(String content) {
        this.content = content;
    }
    // 좋아요 수정
    public void updateLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }
    // 신고수 수정
    public void updateReportCount(int reportCount) {
        this.reportCount = reportCount;
    }
    // 조회수 수정
    public void updateViewCount(int viewCount) {
        this.viewCount = viewCount;
    }
    // 글 상태 수정
    public void updateState(StatusCode state) {
        this.state = state;
    }
}
