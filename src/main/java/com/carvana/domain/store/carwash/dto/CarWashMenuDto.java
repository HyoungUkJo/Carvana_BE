package com.carvana.domain.store.carwash.dto;

import com.carvana.domain.store.carwash.entity.CarWashMenu;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CarWashMenuDto {
    private Long id;                // 메뉴 아이디
    private String menuName;        //메뉴 이름
    private String menuDescription; // 메뉴 설명
    private int price;              // 가격
    private int duration;           // 소요 시간

    @Builder
    public CarWashMenuDto(Long id, String menuName, String menuDescription, int price, int duration) {
        this.id = id;
        this.menuName = menuName;
        this.menuDescription = menuDescription;
        this.price = price;
        this.duration = duration;
    }

    public CarWashMenuDto(CarWashMenu entity) {
        this.id = entity.getId();
        this.menuName = entity.getMenuName();
        this.menuDescription = entity.getMenuDescription();
        this.price = entity.getPrice();
        this.duration = entity.getDuration();
    }
}
