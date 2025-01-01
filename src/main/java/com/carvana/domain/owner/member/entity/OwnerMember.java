package com.carvana.domain.owner.member.entity;

import com.carvana.domain.owner.auth.entity.OwnerAuth;
import com.carvana.domain.reservation.entity.Reservation;
import com.carvana.domain.store.carwash.entity.CarWash;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OwnerMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String phone;

    @OneToMany(mappedBy = "ownerMember")
    private final List<CarWash> carWashes = new ArrayList<>();

//    @OneToMany(mappedBy = "ownerMember")
//    private final List<Reservation> reservationList = new ArrayList<>();

    @OneToOne(mappedBy = "ownerMember")
    private OwnerAuth ownerAuth;

    public OwnerMember(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }
    // 세차장 등록 및 연관관계 설정
    public void addCarWash(CarWash carWash) {
        this.carWashes.add(carWash);
        carWash.setOwnerMember(this);
    }
}
