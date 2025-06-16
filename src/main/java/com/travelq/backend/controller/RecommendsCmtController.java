package com.travelq.backend.controller;

import com.travelq.backend.dto.RecommendCmt.RcmCmtRequestDTO;
import com.travelq.backend.dto.common.ApiResponseDTO;
import com.travelq.backend.service.RecommendCmt.RcmCmtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/api/recommendcmt")
@RequiredArgsConstructor
public class RecommendsCmtController {
    private final RcmCmtService rcmCmtService;

    // 추천 게시글 댓글 등록
    @PostMapping("/")
    public ResponseEntity<ApiResponseDTO<RcmCmtRequestDTO>> createRecommendCmt(@RequestBody @Valid RcmCmtRequestDTO rcmCmtRequestDTO,
                                                                               Authentication authentication) throws IOException {
        log.info("추천 게시글 댓글 등록 --------------------");
        return rcmCmtService.createRcmCmt(rcmCmtRequestDTO, authentication);
    }

    // 추천 게시글 댓글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<?>> deleteRecommendCmt(@PathVariable Long id, Authentication authentication) throws IOException {
        log.info("추천 게시글 댓글 삭제 -------------------");
        return rcmCmtService.deleteRcmCmt(id, authentication);
    }

    // 추천 게시글 댓글 신고
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<?>> reportRecommendCmt(@PathVariable Long id) throws IOException {
        log.info("추천 게시글 댓글 신고 --------------------");
        return rcmCmtService.reportRcmCmt(id);
    }

}
