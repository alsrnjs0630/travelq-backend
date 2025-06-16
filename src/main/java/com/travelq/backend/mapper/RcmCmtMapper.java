package com.travelq.backend.mapper;

import com.travelq.backend.dto.RecommendCmt.RcmCmtResponseDTO;
import com.travelq.backend.entity.RecommendCmt;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RcmCmtMapper {
    // 응답
    @Mapping(source = "recommend.id", target = "recommendId")
    RcmCmtResponseDTO toDTO(RecommendCmt recommendCmt);
}
