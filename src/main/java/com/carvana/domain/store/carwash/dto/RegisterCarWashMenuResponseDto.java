package com.carvana.domain.store.carwash.dto;

import com.carvana.domain.store.carwash.entity.CarWashMenu;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RegisterCarWashMenuResponseDto {
    private Long carWashMenuId;
    private Long carWashId;
}
