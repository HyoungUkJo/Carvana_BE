package com.carvana.domain.store.carwash.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CarWashSearchRequestDto {
    private String name;            // 업체명 키워드
    private String region;          // 시/도
    private String district;        // 구/군
    private Integer minPrice;       // 최소 가격
    private Integer maxPrice;       // 최대 가격
//    private Double minRating;       // 최소 별점 -> 이건 기획에도 없음
    private String menuName;        // 작업 종류
}
