package com.travelq.backend.service.Ask;


import com.travelq.backend.dto.Ask.AskCreateDTO;
import com.travelq.backend.dto.Ask.AskResponseDTO;
import com.travelq.backend.dto.common.ApiResponseDTO;
import com.travelq.backend.dto.common.PageDTO;
import com.travelq.backend.util.PostSearchSpecs;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.Map;


public interface AskService {
    // 게시판 목록 조회
    PageDTO<AskResponseDTO, PostSearchSpecs> getList(PostSearchSpecs postSearchSpecs, Pageable pageable) throws IOException;
    // 게시판 등록
    ResponseEntity<ApiResponseDTO<AskCreateDTO>> createPost(AskCreateDTO askCreateDTO) throws IOException;
}
