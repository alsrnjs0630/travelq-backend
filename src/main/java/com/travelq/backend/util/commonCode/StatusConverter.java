package com.travelq.backend.util.commonCode;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class StatusConverter implements AttributeConverter<StatusCode, String> {

    // DB에 저장하는 값은 code('00', '01', '02')
    @Override
    public String convertToDatabaseColumn(StatusCode statusCode) {
        return statusCode.getCode();
    }

    // DB 저장값을 enum으로 변환
    @Override
    public StatusCode convertToEntityAttribute(String code) {
        return StatusCode.convertCode(code);
    }
}
