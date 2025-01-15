package com.carvana.domain.reservation.dto;

import lombok.Builder;

import java.time.LocalTime;

public class TimeSlot {
    private LocalTime time; // 시간
    private int availableBays;  // 가능한 베이 수

    // private boolean available    // 예약 가능 여부 -> 휴일 등으로 휴무일때를 표시하기 위해 고려중..


    @Builder
    public TimeSlot(LocalTime time, int availableBays) {
        this.time = time;
        this.availableBays = availableBays;
    }
}
