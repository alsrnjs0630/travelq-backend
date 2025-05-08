package com.travelq.backend.service.Member;

import com.travelq.backend.dto.Member.MemberCreateDTO;
import com.travelq.backend.dto.common.ApiResponseDTO;
import org.springframework.http.ResponseEntity;

public interface MemberService {
    ResponseEntity<ApiResponseDTO<MemberCreateDTO>> createMember(MemberCreateDTO memberCreateDTO);
}
