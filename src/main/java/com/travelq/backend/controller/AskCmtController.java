package com.travelq.backend.controller;

import com.travelq.backend.dto.AskCmt.AskCmtRequestDTO;
import com.travelq.backend.dto.common.ApiResponseDTO;
import com.travelq.backend.service.AskCmt.AskCmtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/askcmt")
public class AskCmtController {
    private final AskCmtService askCmtService;

    // 질문 게시판 댓글 작성
    @PostMapping("/")
    public ResponseEntity<ApiResponseDTO<AskCmtRequestDTO>> createAskCmt(@RequestBody @Valid AskCmtRequestDTO askCmtRequestDTO,
                                                                         Authentication authentication) {
        log.info("질문 게시판 댓글 등록 --------------------");
        return askCmtService.createAskCmt(askCmtRequestDTO, authentication);
    }

    // 질문 게시판 댓글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<?>> deleteAskCmt(@PathVariable Long id, Authentication authentication) {
        log.info("질문 게시판 댓글 삭제 -------------------");
        return askCmtService.deleteAskCmt(id, authentication);
    }

    // 질문 게시판 댓글 신고
    @PostMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<?>> reportAskCmt(@PathVariable Long id) {
        log.info("질문 게시판 댓글 신고 -------------------");
        return askCmtService.reportAskCmt(id);
    }
}
