package com.carvana.domain.store.carwash.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SetCarWashTargetRequestDto {
    private Long carWashId;         // 세차장 Id
    private int targetWorkload;     // 목표 작업량
    private int targetRevenue;      // 목표 매출
    private int targetReviews;      // 목표 리뷰
}
