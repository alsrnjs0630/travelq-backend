package com.travelq.backend.service.RecommendCmt;

import com.travelq.backend.dto.Recommend.RcmRequestDTO;
import com.travelq.backend.dto.RecommendCmt.RcmCmtRequestDTO;
import com.travelq.backend.dto.common.ApiResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.io.IOException;

public interface RcmCmtService {
    ResponseEntity<ApiResponseDTO<RcmCmtRequestDTO>> createRcmCmt(RcmCmtRequestDTO rcmCmtRequestDTO, Authentication authentication) throws IOException;
    ResponseEntity<ApiResponseDTO<?>> deleteRcmCmt(Long rcmCmtId, Authentication authentication) throws IOException;
    ResponseEntity<ApiResponseDTO<?>> reportRcmCmt(Long rcmCmtId) throws IOException;
}
