package com.carvana.domain.store.carwash.entity;

import com.carvana.domain.reservation.entity.ReservationMenu;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "carWashMenu")
    private final List<ReservationMenu> reservationMenus = new ArrayList<>();   // 선택된 예약과의 관계 설정

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
