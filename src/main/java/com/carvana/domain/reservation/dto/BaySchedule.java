package com.carvana.domain.reservation.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class BaySchedule {
    private Integer bayNumber;      // 베이 번호
    private List<TimeSlot> availableTimeSlot;      // 가능 여부
}
