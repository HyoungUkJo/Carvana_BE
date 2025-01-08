package com.carvana.domain.reservation.controller;

import com.carvana.domain.reservation.dto.*;
import com.carvana.domain.reservation.entity.ReservationStatus;
import com.carvana.domain.reservation.service.ReservationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "사장님 예약", description = "예약과 관련된 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/owner/reservations")
public class OwnerReservationController {

    private final ReservationService reservationService;
    // owner 홈 화면 (일정,월간 매출 및 작업량, 리뷰)

    // 월간 매출 작업량 리뷰량
    @GetMapping("/getMontlystats/{carWashId}")
    public MonthlyStatsDto getMonthlyStats(@PathVariable Long carWashId) {
        return reservationService.getMonthlyStats(carWashId);
    }

    // 요청된 처리되지 않은 예약 현황 (Pending상태)
    // Todo: 지금은 예약 이미지를 한번에 넘겨주고 있는데 추후에는 요청하면 해당 reservation만 return하는 방식으로 (비용이 비쌈)
    @GetMapping("/getRequestedReservation/{carWashId}")
    public List<ReservationResponseDto> requestedPendingReservation(@PathVariable Long carWashId) {
        return reservationService.requestedPendingReservation(carWashId);
    }

    // 예약 수락 거절
    @PatchMapping("/updateStatus/{reservationId}")
    public ReservationUpdateResponseDto updateReservationStatus(
        @PathVariable Long reservationId, @RequestParam ReservationUpdateRequestDto requestDto) {
        return reservationService.updateReservationstatus(reservationId, requestDto);
    }

}
