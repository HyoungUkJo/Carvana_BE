package com.carvana.domain.reservation.controller;

import com.carvana.domain.reservation.dto.*;
import com.carvana.domain.reservation.entity.ReservationStatus;
import com.carvana.domain.reservation.service.OwnerReservationService;
import com.carvana.domain.reservation.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "사장님 예약", description = "예약과 관련된 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/owner/reservations")
public class OwnerReservationController {

    private final OwnerReservationService ownerReservationService;
    // owner 홈 화면 (일정,월간 매출 및 작업량, 리뷰)

    // 월간 매출 작업량 리뷰량
    @Operation(summary = "월간 매출 작업량 리뷰량", description = "월간 매출 작업량 리뷰량 API")
    @GetMapping("/{carWashId}/stats/monthly")
    public MonthlyStatsDto getMonthlyStats(@PathVariable Long carWashId) {
        return ownerReservationService.getMonthlyStats(carWashId);
    }
    // 월간 예약 현황
    @Operation(summary = "월간 예약 현황", description = "한달 전체의 예약현황 API 요청 일자는 '?year=2025&month=1'")
    @GetMapping("/{carWashId}/monthly")
    public List<ReservationResponseDto> getMonthlyReservations(
        @PathVariable Long carWashId,
        @RequestParam int year,
        @RequestParam int month
    ){
        return ownerReservationService.getMonthlyReservation(carWashId, year, month);
    }

    // 오늘 예약 현황
    @Operation(summary = "오늘 예약 현황", description = "오늘 예약 현황")
    @GetMapping("/{carWashId}/today")
    public List<ReservationResponseDto> getTodayReservations(Long carWashId) {
        return ownerReservationService.getTodayReservation(carWashId);
    }

    // 요청된 처리되지 않은 예약 현황 (Pending상태)
    // Todo: 지금은 예약 이미지를 한번에 넘겨주고 있는데 추후에는 요청하면 해당 reservation만 return하는 방식으로 (비용이 비쌈)
    @Operation(summary = "요청 예약 확인", description = "예약 상태가 PENDING인 예약 현황을 가지고온다.")
    @GetMapping("/getRequestedReservation/{carWashId}")
    public List<ReservationResponseDto> requestedPendingReservation(@PathVariable Long carWashId) {
        return ownerReservationService.requestedPendingReservation(carWashId);
    }

    // 예약 수락 거절
    @Operation(summary = "예약 수락 거절", description = "예약상태     PENDING,    // 대기중 (초기 상태)\n" +
        "    DENIED,     // 예약 거절\n" +
        "    CONFIRMED,  // 예약 확정\n" +
        "    CANCELLED,  // 예약 취소\n" +
        "    COMPLETED   // 서비스 완료 예약 거절은 CANCELLED, 예약 수락은 CONFIRMED")
    @PatchMapping("/updateStatus/{reservationId}")
    public ReservationUpdateResponseDto updateReservationStatus(
        @PathVariable Long reservationId, @RequestBody ReservationUpdateRequestDto requestDto) {
        return ownerReservationService.updateReservationstatus(reservationId, requestDto);
    }

}
