package com.travelq.backend.mapper;

import com.travelq.backend.dto.Recommend.RcmRequestDTO;
import com.travelq.backend.dto.Recommend.RcmResponseDTO;
import com.travelq.backend.entity.Recommend;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = RcmCmtMapper.class)
public interface RcmMapper {
    Recommend toEntity(RcmRequestDTO rcmRequestDTO);

    @Mapping(source = "member.id", target = "memberId")
    @Mapping(source = "recommendCmts", target = "recommendCmts")
    RcmResponseDTO toResponseDTO(Recommend recommend);
}
