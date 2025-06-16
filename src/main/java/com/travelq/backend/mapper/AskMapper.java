package com.travelq.backend.mapper;

import com.travelq.backend.dto.Ask.AskCreateDTO;
import com.travelq.backend.dto.Ask.AskResponseDTO;
import com.travelq.backend.dto.Ask.AskUpdateDTO;
import com.travelq.backend.entity.Ask;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;


@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = false), uses = AskCmtMapper.class)
public interface AskMapper {
    // 등록
    Ask toEntity(AskCreateDTO askCreateDTO);

    // 수정
    Ask toEntity(AskUpdateDTO askUpdateDTO);

    // 응답
    @Mapping(source = "askCmts", target = "cmts")
    AskResponseDTO toResponseDTO(Ask ask);

 /*   // 댓글 수동 매핑
    default AskResponseDTO toResponseDTOWithCmts(Ask ask, AskCmtMapper askCmtMapper) {
        if (ask == null) {
            return null;
        }

        // 기본 필드 자동 매핑
        AskResponseDTO askResponseDTO = toResponseDTO(ask);

        // 댓글 변환
        List<AskCmtResponseDTO> cmt = ask.getAskCmts().stream()
                .map(askCmtMapper::toDto)
                .toList();

        // 댓글 수동 매핑
        return askResponseDTO.toBuilder()
                .cmts(cmt)
                .build();
    }*/
}
