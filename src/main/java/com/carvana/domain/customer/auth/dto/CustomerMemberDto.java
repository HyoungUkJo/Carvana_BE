package com.carvana.domain.customer.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomerMemberDto {
    private String name;
    private String phone;

    private String carType;     // 차종
    private String carNumber;   // 차번호
}
