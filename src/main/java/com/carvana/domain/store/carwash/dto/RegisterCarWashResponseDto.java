package com.carvana.domain.store.carwash.dto;

import com.carvana.domain.store.carwash.entity.CarWash;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class RegisterCarWashResponseDto {
    private Long carWashId;
    private String name;        // 세차장 이름
    private String address;     // 주소
    private String phone;       // 연락처
    private String businessHours;   // 영업 시간
    private Integer bayCount;   // 베이 수
    private String thumbnailImgUrl; // 대표 이미지(썸네일)
    private String uuid;        // 세차장 uuid

    @Builder
    public RegisterCarWashResponseDto(Long carWashId,
                                      String name,
                                      String address,
                                      String phone,
                                      String businessHours,
                                      Integer bayCount,
                                      String thumbnailImgUrl,
                                      String uuid) {
        this.carWashId = carWashId;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.businessHours = businessHours;
        this.bayCount = bayCount;
        this.thumbnailImgUrl = thumbnailImgUrl;
        this.uuid = uuid;
    }
}
