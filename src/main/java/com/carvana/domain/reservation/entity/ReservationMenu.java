package com.carvana.domain.reservation.entity;

import com.carvana.domain.store.carwash.entity.CarWashMenu;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class ReservationMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;        // 예약

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carwash_menu_id")
    private CarWashMenu carWashMenu;        // 세차메뉴

    @Builder
    public ReservationMenu(Reservation reservation, CarWashMenu carWashMenu) {
        this.reservation = reservation;
        this.carWashMenu = carWashMenu;
    }
}
