package com.carvana.domain.store.carwash.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor
@Schema(description = "세차장 등록 요청 DTO")
public class RegisterCarWashRequestDto {
    @Schema(description = "사장 id")
    private Long ownerId; // 임시로 ownerId를 요청

    @Schema(description = "세차장 이름")
    private String name;

    @Schema(description = "세차장 주소")
    private String address;

    @Schema(description = "세차장 번호")
    private String phone;

    @Schema(description = "영업시간")
    private String businessHours;

    @Schema(description = "대표사진")
    private MultipartFile thumbnailImg; // 대표 이미지(썸네일)
}
