package com.carvana.domain.reservation.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservationRequestDto {
    private Long customerMemberID;      // 예약자 정보 -> Todo: 추후 토큰으로 대체
    private Long carWashId;             // 세차장 아이디
    private String carType;             // 차종
    private Long menuId;                // 메뉴 아이디
    private LocalDateTime reservationDateTime;  // 예약일자
    private String request;             //  요청사항
    private Integer bayNumber;          // 예약할 베이 번호
//    private MultipartFile requestImg;   // 사진첨부
}
