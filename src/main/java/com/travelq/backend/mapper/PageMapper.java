package com.travelq.backend.mapper;

import com.travelq.backend.dto.common.PageDTO;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Mapper(componentModel = "spring")
public interface PageMapper {
    default <E,S> PageDTO<E,S> toPageDTO(Page<E> page, S search) {
        if(page == null) return null;

        PageDTO<E,S> pageDTO = PageDTO.<E, S> builder()
                .contents(page.getContent())    // 콘텐츠 매핑
                .pageNumbers(IntStream.rangeClosed(1, page.getTotalPages()).boxed().collect(Collectors.toList()))   // 페이지 번호 매핑
                .prev(page.hasPrevious())   // 이전 페이지
                .next(page.hasNext())       // 다음 페이지
                .totalCount(page.getTotalPages())   // 전체 항목 수
                .totalPage(page.getTotalPages())    // 전체 페이지 수
                .currentPage(page.getNumber() + 1)  // 현재 페이지 설정 (1부터 시작하는 페이지 번호로 변환)
                .prevPage(page.hasPrevious() ? page.getNumber() : null)     // 이전 페이지 번호
                .nextPage(page.hasNext () ? page.getNumber() + 2 : null)    // 다음 페이지 번호
                .search(search)
                .build();

        return pageDTO;
    }
}
