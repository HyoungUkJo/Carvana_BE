package com.carvana.domain.store.carwash.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CarWashMonthlyStatsDto {
    private Integer totalWorkload;      // 작업량
    private Integer targetWorkload;     // 목표 작업량
    private Integer totalRevenue;       // 총 매출
    private Integer targetRevenue;      // 목표 매출
    private Integer totalReviews;       // 총 리뷰
    private Integer targetReviews;      // 목표 리뷰

    @Builder
    public CarWashMonthlyStatsDto(int totalWorkload, int totalRevenue, int totalReviews ,Integer targetWorkload, Integer targetRevenue, Integer targetReviews) {
        this.totalWorkload = totalWorkload;
        this.totalRevenue = totalRevenue;
        this.totalReviews = totalReviews;
        this.targetWorkload = targetWorkload;
        this.targetRevenue = targetRevenue;
        this.targetReviews = targetReviews;
    }
}
