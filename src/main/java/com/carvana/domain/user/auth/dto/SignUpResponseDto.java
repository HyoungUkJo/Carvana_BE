package com.carvana.domain.user.auth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignUpResponseDto {
    private String email;      // 가입된 이메일
    private String name;       // 이름
    private String message;    // 성공 메시지

    @Builder
    public SignUpResponseDto(String email, String name) {
        this.email = email;
        this.name = name;
        this.message = "회원가입이 성공적으로 완료되었습니다.";
    }
}
