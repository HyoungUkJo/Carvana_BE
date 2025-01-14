package com.carvana.domain.reservation.controller;

import com.carvana.domain.reservation.dto.ReservationRequestDto;
import com.carvana.domain.reservation.dto.ReservationResponseDto;
import com.carvana.domain.reservation.service.ReservationService;
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
    @GetMapping("/{carWashId}/availableReservation")
    public void getAvailableTime(@PathVariable Long carWashId,
                                 @RequestParam(required = false) YearMonth yearMonth) {

        //시간과 일자는 오늘 기준으로 서버가 설정
        LocalDateTime nowTime = LocalDateTime.now();
        LocalDate startDate = yearMonth.atDay(nowTime.getDayOfMonth()); // 위의 코드와 중복인지?
        LocalDate endOfDate = yearMonth.atEndOfMonth();

        // default는 현재 날짜 시간을 기준
        if (yearMonth == null) {
            yearMonth = YearMonth.now();
        } else {
            // 만약 다른 달이 들어왔을 경우 시간을 초기화
            startDate = yearMonth.atDay(1);
            nowTime = LocalDateTime.MIN;
        }

        // 날짜를 기준으로 디비 쿼리 조회

        // Booking이 필요함. 30분 단위의 예약 가능여부


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
