package com.travelq.backend.dto.common;

import java.io.Serializable;
import java.util.List;

public class PageDTO<E, S> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 현재 페이지에 표시될 콘텐츠 목록입니다.
     */
    private List<E> contents;

    /**
     * 페이지네이션에 표시할 페이지 번호 목록입니다.
     */
    private List<Integer> pageNumbers;

    /**
     * 이전 페이지가 있는지 여부를 나타냅니다.
     */
    private Boolean prev;

    /**
     * 다음 페이지가 있는지 여부를 나타냅니다.
     */
    private Boolean next;

    /**
     * 전체 항목의 총 개수입니다.
     */
    private Integer totalCount;

    /**
     * 이전 페이지의 페이지 번호입니다.
     */
    private Integer prevPage;

    /**
     * 다음 페이지의 페이지 번호입니다.
     */
    private Integer nextPage;

    /**
     * 전체 페이지 수입니다.
     */
    private Integer totalPage;

    /**
     * 현재 페이지 번호입니다.
     */
    private Integer currentPage;

    private S search;
}
