package com.carvana.domain.store.carwash.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class CarWashMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carwash_id")
    private CarWash carWash;

    private String menuName;        //메뉴 이름
    private String menuDescription; // 메뉴 설명
    private int price;              // 가격
    private int duration;           // 소요 시간

    @Builder
    public CarWashMenu(String menuName, String menuDescription, int price, int duration) {
        this.menuName = menuName;
        this.menuDescription = menuDescription;
        this.price = price;
        this.duration = duration;
    }

    public void setCarWash(CarWash carWash) {
        this.carWash = carWash;
    }
}
