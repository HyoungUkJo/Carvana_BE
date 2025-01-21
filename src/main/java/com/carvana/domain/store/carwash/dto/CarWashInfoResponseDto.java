package com.carvana.domain.store.carwash.dto;

import com.carvana.domain.store.carwash.entity.CarWashMenu;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class CarWashInfoResponseDto {
    private Long id;          // 세차장 id
    private String name;        // 세차장 이름
    private String address;     // 주소
    private String phone;       // 연락처
    private String businessHours;   // 영업 시간
    private Integer bayCount;   // 베이 수
    private String businessNumber;  // 사업자번호
    private String thumbnailImgUrl; // 대표 이미지(썸네일)
    private List<CarWashMenuDto> carWashMenus;

    @Builder
    public CarWashInfoResponseDto(Long id,
                                  String name,
                                  String address,
                                  String phone,
                                  String businessHours,
                                  Integer bayCount,
                                  String businessNumber,
                                  String thumbnailImgUrl,
                                  List<CarWashMenuDto> carWashMenus) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.businessHours = businessHours;
        this.bayCount = bayCount;
        this.businessNumber = businessNumber;
        this.thumbnailImgUrl = thumbnailImgUrl;
        this.carWashMenus = carWashMenus;
    }
}
