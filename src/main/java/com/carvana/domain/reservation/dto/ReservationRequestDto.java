package com.carvana.domain.reservation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "예약 요청 DTO")
@Setter
public class ReservationRequestDto {
    @Schema(description = "예약자 ID")
    private Long customerMemberID;

    @Schema(description = "세차장 ID")
    private Long carWashId;

    @Schema(description = "차종")
    private String carType;

    @Schema(description = "차량 번호")
    private String carNumber;

    @Schema(description = "선택한 메뉴 ID 목록")
    private List<Long> menuIds;

    @Schema(description = "예약 일시")
    private LocalDateTime reservationDateTime;

    @Schema(description = "요청사항")
    private String request;

    @Schema(description = "베이 번호")
    private Integer bayNumber;

    @Schema(description = "차량 이미지 파일들 (선택사항)", nullable = true)
    private List<MultipartFile> images;
}
