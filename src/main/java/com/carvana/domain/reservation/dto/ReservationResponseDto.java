package com.carvana.domain.reservation.dto;

import com.carvana.domain.reservation.entity.ReservationStatus;
import com.carvana.domain.store.carwash.entity.CarWash;
import com.carvana.domain.store.carwash.entity.CarWashMenu;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/*
* Todo: 고민해야할 사항
*  성공 여부만 전송할것인지 요청한 데이터를 검증할 겸 그대로 보내줄것인지 확인
* */
@Getter
@NoArgsConstructor
public class ReservationResponseDto {
    private Long reservationId;
    private LocalDateTime reservationDateTime;
    private String request;
    private String imageUrl;
    private String carType;
    private String carNumber;   //  차번호
    private ReservationStatus status;
    private List<String> menuList;       // 예약한 메뉴

    @Builder
    public ReservationResponseDto(Long reservationId, LocalDateTime reservationDateTime,
                                  String request, String imageUrl, String carType, String carNumber,
                                  ReservationStatus status, List<String> menuList) {
        this.reservationId = reservationId;
        this.reservationDateTime = reservationDateTime;
        this.request = request;
        this.imageUrl = imageUrl;
        this.carType = carType;
        this.carNumber = carNumber;
        this.status = status;
        this.menuList = menuList;
    }
}
