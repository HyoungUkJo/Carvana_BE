package com.carvana.domain.customer.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CustomerMemberProfileUpdateRequestDto {
    private String name;
    private String phone;
    private String carType;     // 차종
    private String carNumber;   // 차번호
}
