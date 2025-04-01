package com.carvana.domain.store.carwash.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class CarWashProfileUpdateRequestDto {
    private Long carWashId;  // 세차장 ID -> 추후 UUID 같은걸로
    private String name;        // 세차장 이름
    private String address;     // 주소
    private String phone;       // 연락처
    private String businessHours;   // 영업 시간
    private Integer bayCount;   // 베이 수
    private String businessNumber; //사업자 번호
    private MultipartFile thumbnailImg; // 대표 이미지(썸네일)
}
