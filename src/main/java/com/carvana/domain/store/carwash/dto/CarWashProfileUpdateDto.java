package com.carvana.domain.store.carwash.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Builder
public class CarWashProfileUpdateDto {
    private String name;        // 세차장 이름
    private String address;     // 주소
    private String phone;       // 연락처
    private String businessHours;   // 영업 시간
    private Integer bayCount;   // 베이 수
    private String businessNumber; //사업자 번호
    private String thumbnailImgKey; // 대표 이미지(썸네일)

}
