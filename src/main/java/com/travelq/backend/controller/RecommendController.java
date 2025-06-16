package com.travelq.backend.controller;

import com.travelq.backend.dto.Recommend.RcmRequestDTO;
import com.travelq.backend.dto.Recommend.RcmResponseDTO;
import com.travelq.backend.dto.Recommend.RcmUpdateDTO;
import com.travelq.backend.dto.common.ApiResponseDTO;
import com.travelq.backend.dto.common.PageDTO;
import com.travelq.backend.service.Recommend.RcmService;
import com.travelq.backend.util.search.PostSearchSpecs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recommends")
public class RecommendController {

    private final RcmService rcmService;

    // 추천 게시글 목록
    @GetMapping("/list")
    public PageDTO<RcmResponseDTO, PostSearchSpecs> getRecommendList(@ModelAttribute PostSearchSpecs postSearchSpecs,
                                                                     @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("추천 게시글 목록 --------------------");
        return rcmService.getRecommendList(postSearchSpecs, pageable);
    }

    // 추천 게시글 조회
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<RcmResponseDTO>> getRecommend(@PathVariable Long id) {
        log.info("추천 게시글 상세 --------------------");
        return rcmService.getRecommend(id);
    }

    // 추천 게시글 등록
    @PostMapping("/")
    public ResponseEntity<ApiResponseDTO<RcmRequestDTO>> createRecommendPost(@ModelAttribute RcmRequestDTO rcmRequestDTO,
                                                                             Authentication authentication) throws IOException {
        log.info("추천 게시글 등록 -------------------");
        return rcmService.createRcm(rcmRequestDTO, authentication);
    }

    // 추천 게시글 수정 권한 확인
    @GetMapping("/{id}/authcheck")
    public ResponseEntity<ApiResponseDTO<?>> checkAuth(@PathVariable Long id, Authentication authentication) throws IOException {
        log.info("추천 게시글 수정 권한 확인 --------------------");
        return rcmService.updateAuthCheck(id, authentication);
    }

    // 추천 게시글 수정
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<RcmUpdateDTO>> updateRecommendPost(@PathVariable Long id,
                                                                             @ModelAttribute RcmUpdateDTO rcmUpdateDTO,
                                                                             Authentication authentication) throws IOException {
        log.info("추천 게시글 수정 --------------------");
        return rcmService.updateRcm(id, rcmUpdateDTO, authentication);
    }

    // 추천 게시글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<?>> deleteRecommendPost(@PathVariable Long id,
                                                                 Authentication authentication) throws IOException {
        log.info("추천 게시글 삭제 --------------------");
        return rcmService.deleteRcm(id, authentication);
    }
}
