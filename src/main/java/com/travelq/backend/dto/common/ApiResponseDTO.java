package com.travelq.backend.dto.common;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponseDTO<T> {
    private boolean success;    // 성공 여부
    private String message;     // 설명 메시지
    private T data;             // 응답 데이터
}
