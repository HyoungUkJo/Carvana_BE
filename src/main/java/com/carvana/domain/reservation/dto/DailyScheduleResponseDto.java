package com.carvana.domain.reservation.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class DailyScheduleResponseDto {
    private LocalDate date;                 // 선택한 날자
    private List<BaySchedule> baySchedules; // 베이의 예약가능한 시간
}
