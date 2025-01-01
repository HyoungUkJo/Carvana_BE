package com.carvana.domain.store.carwash.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RegisterCarWashMenuRequestDto {
    private String menuName;        //메뉴 이름
    private String menuDescription; // 메뉴 설명
    private int price;              // 가격
    private int duration;           // 소요 시간
}
