package com.travelq.backend.controller;

import com.travelq.backend.dto.Ask.AskCreateDTO;
import com.travelq.backend.dto.Ask.AskResponseDTO;
import com.travelq.backend.dto.Ask.AskUpdateDTO;
import com.travelq.backend.dto.common.ApiResponseDTO;
import com.travelq.backend.dto.common.PageDTO;
import com.travelq.backend.entity.Ask;
import com.travelq.backend.service.Ask.AskService;
import com.travelq.backend.util.PostSearchSpecs;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ask")
public class AskController {
    private final AskService askService;

    // 게시판 목록 조회
    @GetMapping("/list")
    public PageDTO<AskResponseDTO, PostSearchSpecs> getAskList(@ModelAttribute PostSearchSpecs postSearchSpecs,
                                                               @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) throws IOException {
        log.info("질문 게시글 목록----------------------------------------------------------");
        return askService.getList(postSearchSpecs, pageable);
    }

    // 게시글 등록
    @PostMapping("/")
    public ResponseEntity<ApiResponseDTO<AskCreateDTO>> createAskPost(@RequestBody @Valid AskCreateDTO askCreateDTO) throws IOException {
        log.info("질문 게시글 등록----------------------------------------------------------");
        return askService.createPost(askCreateDTO);
    }

    // 게시판 수정
    @PutMapping("/{askId}")
    public ResponseEntity<ApiResponseDTO<AskUpdateDTO>> updateAskPost(@PathVariable Long askId, @RequestBody @Valid AskUpdateDTO askUpdateDTO) throws IOException {
        log.info("질문 게시글 수정----------------------------------------------------------");
        return askService.updatePost(askId, askUpdateDTO);
    }

    // 게시글 삭제
    @DeleteMapping("/{askId}")
    public ResponseEntity<ApiResponseDTO<?>> deleteAskPost(@PathVariable Long askId) throws IOException {
        log.info("질문 게시글 삭제----------------------------------------------------------");
        return askService.deletePost(askId);
    }
}
