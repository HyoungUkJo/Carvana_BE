package com.carvana.domain.reservation.controller;

import com.carvana.domain.reservation.dto.DailyScheduleResponseDto;
import com.carvana.domain.reservation.dto.ReservationRequestDto;
import com.carvana.domain.reservation.dto.ReservationResponseDto;
import com.carvana.domain.reservation.service.ReservationService;
import com.carvana.global.exception.custom.ReservationException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "손님 예약", description = "예약과 관련된 API")
@RestController
@Slf4j
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
    @Operation(summary = "예약 생성", description = "차량 이미지와 함께 새로운 예약을 생성합니다")
    @PostMapping(value = "/reserve", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ReservationResponseDto createReservation(
        @ModelAttribute ReservationRequestDto requestDto,
        @RequestPart(value = "images", required = false) List<MultipartFile> images
    ) {
        // 디버깅용
        System.out.println("=== Request received ===");
        if (requestDto.getImages() == null) {
            System.out.println("Images is null");
            throw new ReservationException("이미지가 넘어오지 않았음");
        } else {
            System.out.println("Images size: " + requestDto.getImages().size());
            requestDto.getImages().forEach(image ->
                System.out.println("Image name: " + image.getOriginalFilename() + ", size: " + image.getSize())
            );
        }
        return reservationService.createReservation(requestDto);
    }

    // 내 예약 확인
    @GetMapping("/myReservation/{customerId}")
    public List<ReservationResponseDto> getMyReservation(@PathVariable Long customerId) {
        return reservationService.getMyReservation(customerId);
    }

}
