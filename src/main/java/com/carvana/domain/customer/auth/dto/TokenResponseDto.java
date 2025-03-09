package com.carvana.domain.customer.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class TokenResponseDto {
    private String accessToken;
    private String refreshToken;
}
