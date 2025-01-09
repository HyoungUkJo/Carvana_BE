package com.carvana.domain.store.carwash.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RegisterCarWashRequestDto {
    private Long ownerId; // 임시로 ownerId를 요청
    private String name;
    private String address;
    private String phone;
    private String businessHours;
    private String thumbnailImgUrl; // 대표 이미지(썸네일)
}
