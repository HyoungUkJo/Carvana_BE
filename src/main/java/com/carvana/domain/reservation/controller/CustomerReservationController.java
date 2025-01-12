package com.carvana.domain.reservation.controller;

import com.carvana.domain.reservation.dto.ReservationRequestDto;
import com.carvana.domain.reservation.dto.ReservationResponseDto;
import com.carvana.domain.reservation.service.ReservationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "손님 예약", description = "예약과 관련된 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customer/reservations")
public class CustomerReservationController {

    private final ReservationService reservationService;

    // 예약 요청
    @PostMapping("/requestReservation")
    public ReservationResponseDto createReservation(@RequestBody ReservationRequestDto requestDto){
        /*
        * TODO : 데이터베이스 로직 연결
        *  예약 시간 검증
        *  가게 검증
        * */
        return reservationService.createReservation(requestDto);
    }

    // 내 예약 확인
    @GetMapping("/myReservation/{customerId}")
    public List<ReservationResponseDto> getMyReservation(@PathVariable Long customerId) {
        return reservationService.getMyReservation(customerId);
    }

}
