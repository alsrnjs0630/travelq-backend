package com.travelq.backend.service.AskCmt;

import com.travelq.backend.dto.AskCmt.AskCmtRequestDTO;
import com.travelq.backend.dto.common.ApiResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

public interface AskCmtService {
    // 질문 게시판 댓글 등록
    public ResponseEntity<ApiResponseDTO<AskCmtRequestDTO>> createAskCmt(AskCmtRequestDTO askCmtRequestDTO, Authentication authentication);
    // 질문 게시판 댓글 삭제
    public ResponseEntity<ApiResponseDTO<?>> deleteAskCmt(Long askCmtId, Authentication authentication);
    // 질문 게시판 댓글 신고
    public ResponseEntity<ApiResponseDTO<?>> reportAskCmt(Long askCmtId);
}
