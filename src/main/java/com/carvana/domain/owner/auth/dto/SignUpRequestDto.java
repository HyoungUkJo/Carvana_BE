package com.carvana.domain.owner.auth.dto;

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
    private String typeOfBusiness;  // 업종 -> TODO : 논의 필요 -> 업종이 두개일 수도 있다고 생각함.
    private String address;         // 주소
}
