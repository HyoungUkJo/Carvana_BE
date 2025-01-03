package com.carvana.domain.reservation.controller;

import com.carvana.domain.reservation.dto.ReservationRequestDto;
import com.carvana.domain.reservation.dto.ReservationResponseDto;
import com.carvana.domain.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/reservations")
public class ReservationController {

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
}
