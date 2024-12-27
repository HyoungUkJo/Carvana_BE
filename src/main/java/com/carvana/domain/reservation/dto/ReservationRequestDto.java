package com.carvana.domain.reservation.dto;

import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

public class ReservationRequestDto {
    private Long memberID;      // 예약자 정보
    private LocalDateTime reservationDateTime;  // 예약일자
    private String request;     //  요청사항
    private MultipartFile requestImg;    // 사진첨부


    @Builder
    public ReservationRequestDto(Long memberID, LocalDateTime reservationDateTime,
                                 String request, MultipartFile requestImg) {
        this.memberID = memberID;
        this.reservationDateTime = reservationDateTime;
        this.request = request;
        this.requestImg = requestImg;
    }
}
