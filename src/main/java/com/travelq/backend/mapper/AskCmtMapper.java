package com.travelq.backend.mapper;

import com.travelq.backend.dto.AskCmt.AskCmtRequestDTO;
import com.travelq.backend.dto.AskCmt.AskCmtResponseDTO;
import com.travelq.backend.entity.AskCmt;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = false))
public interface AskCmtMapper {
    // 등록
    AskCmt toEntity(AskCmtRequestDTO askCmtRequestDTO);
    // 응답
    @Mapping(source = "ask.id", target = "askId")
    AskCmtResponseDTO toDto(AskCmt askCmt);
}
