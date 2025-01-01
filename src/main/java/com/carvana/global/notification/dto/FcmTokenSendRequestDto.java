package com.carvana.global.notification.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FcmTokenSendRequestDto {
    private Long id;    // 유저 토큰 전 임시로 사용할 id필드 추후 JWT 토큰으로 대체
    private String token;
}
