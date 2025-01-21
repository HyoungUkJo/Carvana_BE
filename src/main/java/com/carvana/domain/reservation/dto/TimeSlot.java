package com.carvana.domain.reservation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;

@Getter
@Builder
public class TimeSlot {
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime time; // 시간
    private boolean available;    // 예약 가능 여부 -> 휴일 등으로 휴무일때를 표시하기 위해 고려중..
}
