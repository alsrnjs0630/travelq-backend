package com.travelq.backend.controller;

import com.travelq.backend.dto.Ask.AskCreateDTO;
import com.travelq.backend.dto.Ask.AskResponseDTO;
import com.travelq.backend.dto.Ask.AskUpdateDTO;
import com.travelq.backend.dto.common.ApiResponseDTO;
import com.travelq.backend.dto.common.PageDTO;
import com.travelq.backend.service.Ask.AskService;
import com.travelq.backend.util.search.PostSearchSpecs;
import jakarta.validation.Valid;
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
@RequestMapping("/api/asks")
public class AskController {
    private final AskService askService;

    // 게시판 목록 조회
    @GetMapping("/list")
    public PageDTO<AskResponseDTO, PostSearchSpecs> getAskList(@ModelAttribute PostSearchSpecs postSearchSpecs,
                                                               @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) throws IOException {
        log.info("질문 게시글 목록----------------------------------------------------------");
        return askService.getList(postSearchSpecs, pageable);
    }

    // 게시글 상세페이지
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<AskResponseDTO>> getAsk(@PathVariable Long id) throws IOException {
        log.info("질문 게시글 상세----------------------------------------------------------");
        return askService.detailPost(id);
    }

    // 게시글 등록
    @PostMapping("/")
    public ResponseEntity<ApiResponseDTO<AskCreateDTO>> createAskPost(@RequestBody @Valid AskCreateDTO askCreateDTO,
                                                                      Authentication authentication) throws IOException {
        log.info("질문 게시글 등록----------------------------------------------------------");
        return askService.createPost(askCreateDTO, authentication);
    }

    // 게시판 수정 권한 체크
    @GetMapping("/{id}/authcheck")
    public ResponseEntity<ApiResponseDTO<?>> checkAuth(@PathVariable Long id, Authentication authentication) throws IOException {
        return askService.checkAuth(id, authentication);
    }

    // 게시판 수정
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<AskUpdateDTO>> updateAskPost(@PathVariable long id,
                                                                      @RequestBody @Valid AskUpdateDTO askUpdateDTO,
                                                                      Authentication authentication) throws IOException {
        log.info("질문 게시글 수정----------------------------------------------------------");
        return askService.updatePost(id, askUpdateDTO, authentication);
    }

    // 게시글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<?>> deleteAskPost(@PathVariable Long id, Authentication authentication) throws IOException {
        log.info("질문 게시글 삭제----------------------------------------------------------");
        return askService.deletePost(id, authentication);
    }

    // 게시글 신고
    @PostMapping("/{id}/report")
    public ResponseEntity<ApiResponseDTO<?>> reportAskPost(@PathVariable Long id) throws IOException {
        log.info("질문 게시글 신고----------------------------------------------------------");
        return askService.reportPost(id);
    }
}
