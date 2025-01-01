package com.carvana.domain.owner.auth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignInResponseDto {
    private String name;
    private String message;

    @Builder
    public SignInResponseDto(String name, String message) {
        this.name = name;
        this.message = "로그인에 성공했습니다.";
    }
}
