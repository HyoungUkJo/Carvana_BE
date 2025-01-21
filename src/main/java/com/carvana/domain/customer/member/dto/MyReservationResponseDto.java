package com.carvana.domain.customer.member.dto;

import com.carvana.domain.reservation.entity.ReservationStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class MyReservationResponseDto {
    private Long reservationId;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime reservationDateTime;
    private String request;
    private String imageUrl;
    private String carType;
    private String carNumber;   //  차번호
    private ReservationStatus status; // 예약 상태
    private List<String> menuList;       // 예약한 메뉴
    private Integer bayNumber;              // 예약한 베이 번호
    private String storeName;

    @Builder
    public MyReservationResponseDto(Long reservationId,
                                    LocalDateTime reservationDateTime,
                                    String request,
                                    String imageUrl,
                                    String carType,
                                    String carNumber,
                                    ReservationStatus status,
                                    List<String> menuList,
                                    Integer bayNumber,
                                    String storeName) {
        this.reservationId = reservationId;
        this.reservationDateTime = reservationDateTime;
        this.request = request;
        this.imageUrl = imageUrl;
        this.carType = carType;
        this.carNumber = carNumber;
        this.status = status;
        this.menuList = menuList;
        this.bayNumber = bayNumber;
        this.storeName = storeName;
    }
}
