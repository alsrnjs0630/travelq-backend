package com.travelq.backend.service.Recommend;

import com.travelq.backend.dto.Recommend.RcmRequestDTO;
import com.travelq.backend.dto.Recommend.RcmResponseDTO;
import com.travelq.backend.dto.Recommend.RcmUpdateDTO;
import com.travelq.backend.dto.common.ApiResponseDTO;
import com.travelq.backend.dto.common.PageDTO;
import com.travelq.backend.util.search.PostSearchSpecs;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.io.IOException;

public interface RcmService {
    // 추천 게시글 목록
    PageDTO<RcmResponseDTO, PostSearchSpecs> getRecommendList(PostSearchSpecs postSearchSpecs, Pageable pageable);
    // 추천 게시글 조회
    ResponseEntity<ApiResponseDTO<RcmResponseDTO>> getRecommend (Long id);
    // 추천 게시글 등록
    ResponseEntity<ApiResponseDTO<RcmRequestDTO>> createRcm(RcmRequestDTO rcmRequestDTO, Authentication authentication) throws IOException;
    // 추천 게시글 수정 권한 확인
    ResponseEntity<ApiResponseDTO<?>> updateAuthCheck(Long id, Authentication authentication) throws IOException;
    // 추천 게시글 수정
    ResponseEntity<ApiResponseDTO<RcmUpdateDTO>> updateRcm(Long id, RcmUpdateDTO rcmUpdateDTO, Authentication authentication) throws IOException;
    // 추천 게시글 삭제
    ResponseEntity<ApiResponseDTO<?>> deleteRcm(Long id, Authentication authentication) throws IOException;
}
