package com.travelq.backend.util.commonCode;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum StatusCode {
    NORMAL("00", "정상"),
    DELETED("01", "삭제"),
    REPORTED("02", "신고");

    private String code;
    private String desc;

    StatusCode(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    // DB값('00', '01', '02')을 enum 코드(NORMAL, DELETED, REPORTED)로 변환
    public static StatusCode convertCode(String code) {
        return Arrays.stream(StatusCode.values())
                .filter(status -> status.code.equals(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("잘못 된 상태코드입니다."));
    }
}
