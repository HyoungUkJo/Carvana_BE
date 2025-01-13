package com.carvana.domain.store.carwash.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class CarWashBusinessTarget {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="carwash")
    private CarWash carWash;

    private Integer monthlyReviewTarget;
    private Integer monthlyRevenueTarget;
    private Integer monthlyWorkloadTarget;

    @Builder
    public CarWashBusinessTarget(CarWash carWash,
                          Integer monthlyReviewTarget,
                          Integer monthlyRevenueTarget,
                          Integer monthlyWorkloadTarget) {
        this.carWash = carWash;
        this.monthlyReviewTarget = monthlyReviewTarget;
        this.monthlyRevenueTarget = monthlyRevenueTarget;
        this.monthlyWorkloadTarget = monthlyWorkloadTarget;
    }

    // 목표치 수정
    public void updateTargets(int reviewTarget, int salesTarget, int workloadTarget) {
        this.monthlyReviewTarget = reviewTarget;
        this.monthlyRevenueTarget = salesTarget;
        this.monthlyWorkloadTarget = workloadTarget;
    }
}
