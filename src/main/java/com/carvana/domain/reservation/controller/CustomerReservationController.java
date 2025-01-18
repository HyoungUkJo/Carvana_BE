package com.carvana.domain.reservation.controller;

import com.carvana.domain.reservation.dto.DailyScheduleResponseDto;
import com.carvana.domain.reservation.dto.ReservationRequestDto;
import com.carvana.domain.reservation.dto.ReservationResponseDto;
import com.carvana.domain.reservation.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

@Tag(name = "손님 예약", description = "예약과 관련된 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customer/reservations")
public class CustomerReservationController {

    private final ReservationService reservationService;

    // 예약 가능일지 요청
    @Operation(summary = "해당 일자에 예약이 가능한지 확인", description = "예약 월 일을 선택하면 예약 가능한 시간은 return 해주는 api")
    @GetMapping("/{carWashId}/availableTime")
    public DailyScheduleResponseDto getAvailableTime(@PathVariable Long carWashId,
                                                       @RequestParam LocalDate date) {
        return reservationService.getAvailableReservation(carWashId, date);

    }


    // 예약 요청
    @PostMapping("/creatReservation")
    public ReservationResponseDto createReservation(@RequestBody ReservationRequestDto requestDto){
        return reservationService.createReservation(requestDto);
    }

    // 내 예약 확인
    @GetMapping("/myReservation/{customerId}")
    public List<ReservationResponseDto> getMyReservation(@PathVariable Long customerId) {
        return reservationService.getMyReservation(customerId);
    }

}
