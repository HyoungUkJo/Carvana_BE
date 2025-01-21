package com.carvana.domain.reservation.dto;

import com.carvana.domain.reservation.entity.ReservationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReservationUpdateRequestDto {
    @Schema(description = "예약 상태[" +
        "    PENDING,    // 대기중 (초기 상태)\n" +
        "    DENIED,     // 예약 거절\n" +
        "    CONFIRMED,  // 예약 확정\n" +
        "    CANCELLED,  // 예약 취소\n" +
        "    COMPLETED   // 서비스 완료]",
    example = "CONFIRMED | DENIED")
    private ReservationStatus status;

    @Schema(description = "예약 거절 사유 (수락시 null로 전송)")
    private String rejectReason;
}
