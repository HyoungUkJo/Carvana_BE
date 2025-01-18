package com.carvana.domain.customer.auth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignUpRequestDto {
    private String email;
    private String password;
    private String name;
    private String phone;
    private String carType;     // 차종
    private String carNumber;   // 차번호

}
