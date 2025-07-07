package com.carvana.domain.customer.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailCheckResponseDto {
    private boolean exists;

    public EmailCheckResponseDto(boolean exists) {
        this.exists = exists;
    }
}
