package com.carvana.domain.reservation.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MonthlyStatsDto {
    private int totalReservation;   // 예약수
    private int totalRevenue;       // 총 매출
    private int totalReviews;       // 총 리뷰

    @Builder
    public MonthlyStatsDto(int totalReservation, int totalRevenue, int totalReviews) {
        this.totalReservation = totalReservation;
        this.totalRevenue = totalRevenue;
        this.totalReviews = totalReviews;
    }
}
