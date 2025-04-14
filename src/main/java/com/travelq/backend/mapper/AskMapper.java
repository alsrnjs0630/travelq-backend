package com.travelq.backend.mapper;

import com.travelq.backend.dto.Ask.AskCreateDTO;
import com.travelq.backend.dto.Ask.AskResponseDTO;
import com.travelq.backend.dto.Ask.AskUpdateDTO;
import com.travelq.backend.entity.Ask;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = false))
public interface AskMapper {
    // 등록
    Ask toEntity(AskCreateDTO askCreateDTO);
    // 수정
    Ask toEntity(AskUpdateDTO askUpdateDTO);
    // 응답
    @Mapping(source = "member.id", target = "memberId")
    AskResponseDTO toResponseDTO(Ask ask);
}
