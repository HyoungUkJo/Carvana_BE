package com.carvana.domain.store.carwash.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
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

    public void setCarWash(CarWash carWash) {
        this.carWash = carWash;
    }
}
