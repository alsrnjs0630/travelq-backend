package com.travelq.backend.util;

import org.springframework.data.jpa.domain.Specification;

public class PostSpecs {
    public static <T> Specification<T> bySearch(PostSearchSpecs dto) {
        return (root, query, criteriaBuilder) -> {
            Specification<T> spec = Specification.where(null);

            // title 필터 추가
            if(dto.getTitle() != null && !dto.getTitle().isEmpty()) {
                spec = spec.and((root1, query1, cb)
                        -> criteriaBuilder.like(root.get("title"), "%" + dto.getTitle() + "%"));
            }

            // author 필터 추가
            if(dto.getAuthor() != null && !dto.getAuthor().isEmpty()) {
                spec = spec.and((root1, query1, cb)
                        -> criteriaBuilder.like(root.get("author"), "%" + dto.getAuthor() + "%"));
            }

            return spec.toPredicate(root, query, criteriaBuilder);
        };
    }
}
