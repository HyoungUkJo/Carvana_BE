package com.carvana.domain.owner.member.entity;

import com.carvana.domain.owner.auth.entity.OwnerAuth;
import com.carvana.domain.reservation.entity.Reservation;
import com.carvana.domain.store.carwash.entity.CarWash;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
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

    private String businessNumber; //사업자 번호

    private String address;         // 집주소

    private String typeOfBusiness;  // 업종

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

    @Builder
    public OwnerMember(String name,
                       String phone,
                       String businessNumber,
                       String address,
                       String typeOfBusiness,
                       OwnerAuth ownerAuth) {
        this.name = name;
        this.phone = phone;
        this.businessNumber = businessNumber;
        this.address = address;
        this.typeOfBusiness = typeOfBusiness;
        this.ownerAuth = ownerAuth;
    }

    public void setBusinessNumber(String businessNumber) {
        this.businessNumber = businessNumber;
    }
}
