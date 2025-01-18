package com.carvana.domain.customer.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CustomerMemberProfileResponseDto {
    private Long id;
    private String name;
    private String phone;

    private String carType;     // 차종
    private String carNumber;   // 차번호

    @Builder
    public CustomerMemberProfileResponseDto(Long id, String name, String phone, String carType, String carNumber) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.carType = carType;
        this.carNumber = carNumber;
    }
}
