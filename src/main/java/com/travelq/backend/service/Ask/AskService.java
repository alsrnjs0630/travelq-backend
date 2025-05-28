package com.travelq.backend.service.Ask;


import com.travelq.backend.dto.Ask.AskCreateDTO;
import com.travelq.backend.dto.Ask.AskResponseDTO;
import com.travelq.backend.dto.Ask.AskUpdateDTO;
import com.travelq.backend.dto.common.ApiResponseDTO;
import com.travelq.backend.dto.common.PageDTO;
import com.travelq.backend.util.search.PostSearchSpecs;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.io.IOException;


public interface AskService {
    // 게시판 목록 조회
    PageDTO<AskResponseDTO, PostSearchSpecs> getList(PostSearchSpecs postSearchSpecs, Pageable pageable) throws IOException;
    // 게시판 상세
    ResponseEntity<ApiResponseDTO<AskResponseDTO>> detailPost(Long id) throws IOException;
    // 게시판 등록
    ResponseEntity<ApiResponseDTO<AskCreateDTO>> createPost(AskCreateDTO askCreateDTO, Authentication authentication) throws IOException;
    // 게시판 수정 권한 확인
    ResponseEntity<ApiResponseDTO<?>> checkAuth(Long id, Authentication authentication) throws IOException;
    // 게시판 수정
    ResponseEntity<ApiResponseDTO<AskUpdateDTO>> updatePost(Long askId, AskUpdateDTO askUpdateDTO, Authentication authentication) throws IOException;
    // 게시판 삭제 (게시물의 상태만 변경됨)
    ResponseEntity<ApiResponseDTO<?>> deletePost(Long id, Authentication authentication) throws IOException;
    // 게시글 신고
    ResponseEntity<ApiResponseDTO<?>> reportPost(Long id) throws IOException;
}
