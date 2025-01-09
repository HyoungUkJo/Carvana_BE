package com.carvana.domain.store.carwash.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CarWashProfileResponseDto {
    private String name;        // 세차장 이름
    private String address;     // 주소
    private String phone;       // 연락처
    private String businessHours;   // 영업 시간
    private Integer bayCount;   // 베이 수
    private String businessNumber;  // 사업자번호
    private String thumbnailImgUrl; // 대표 이미지(썸네일)

    @Builder
    public CarWashProfileResponseDto(String name,
                                     String address,
                                     String phone,
                                     String businessHours,
                                     Integer bayCount,
                                     String businessNumber,
                                     String thumbnailImgUrl) {

        this.name = name;
        this.address = address;
        this.phone = phone;
        this.businessHours = businessHours;
        this.bayCount = bayCount;
        this.businessNumber = businessNumber;
        this.thumbnailImgUrl = thumbnailImgUrl;

    }
}
