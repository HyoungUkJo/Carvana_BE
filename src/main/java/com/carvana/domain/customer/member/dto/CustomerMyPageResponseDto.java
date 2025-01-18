package com.carvana.domain.customer.member.dto;

import com.carvana.domain.reservation.dto.ReservationResponseDto;
import com.carvana.domain.reservation.entity.Reservation;
import com.carvana.domain.review.dto.ReviewResponseDto;
import com.carvana.domain.review.entity.Review;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class CustomerMyPageResponseDto {
    private String name;

    private String phone;

    private String carType;     // 차종

    private String carNumber;   // 차번호

    private List<ReservationResponseDto> reservations; // 이용한 업체 -> 세차가 완료된 세차장만

    private List<ReviewResponseDto> reviews;           // 내가 남긴 리뷰


    @Builder
    public CustomerMyPageResponseDto(String name,
                                     String phone,
                                     String carType,
                                     String carNumber,
                                     List<ReservationResponseDto> reservations,
                                     List<ReviewResponseDto> reviews) {
        this.name = name;
        this.phone = phone;
        this.carType = carType;
        this.carNumber = carNumber;
        this.reservations = reservations;
        this.reviews = reviews;
    }
}
