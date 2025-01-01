package com.carvana.domain.store.carwash.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RegisterCarWashRequestDto {
    private String name;
    private String address;
    private String phone;
    private String businessHours;
}
