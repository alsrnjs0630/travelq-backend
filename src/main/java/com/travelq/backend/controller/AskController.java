package com.travelq.backend.controller;

import com.travelq.backend.dto.Ask.AskCreateDTO;
import com.travelq.backend.dto.Ask.AskResponseDTO;
import com.travelq.backend.dto.common.ApiResponseDTO;
import com.travelq.backend.dto.common.PageDTO;
import com.travelq.backend.entity.Ask;
import com.travelq.backend.service.Ask.AskService;
import com.travelq.backend.util.PostSearchSpecs;
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
        return askService.getList(postSearchSpecs, pageable);
    }

    // 게시글 등록
    @PostMapping("/")
    public ResponseEntity<ApiResponseDTO<AskCreateDTO>> createAskPost(@RequestBody AskCreateDTO askCreateDTO) throws IOException {
        log.info("질문 게시글 등록");
        return askService.createPost(askCreateDTO);
    }
}
