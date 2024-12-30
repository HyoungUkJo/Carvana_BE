package com.carvana.global.exception.custom.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ErrorResponseDto {
    private String code;
    private String message;

    public ErrorResponseDto(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
