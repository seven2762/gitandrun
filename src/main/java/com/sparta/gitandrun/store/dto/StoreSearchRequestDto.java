package com.sparta.gitandrun.store.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class StoreSearchRequestDto {
    private String keyword; // 검색어
    private String sort;    // 정렬 기준 ("createdAt" 또는 "updatedAt")
    private int pageSize;   // 페이지당 건수 (10, 30, 50만 허용)
    private int page;       // 페이지 번호 (0부터 시작)
}
