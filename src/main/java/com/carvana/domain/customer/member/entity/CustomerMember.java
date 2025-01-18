package com.carvana.domain.customer.member.entity;

import com.carvana.domain.customer.auth.entity.CustomerAuth;
import com.carvana.domain.customer.member.dto.CustomerMemberProfileUpdateRequestDto;
import com.carvana.domain.reservation.entity.Reservation;
import com.carvana.domain.review.entity.Review;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomerMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String phone;

    private String carType;     // 차종
    private String carNumber;   // 차번호


    @OneToMany(mappedBy = "customerMember")
    private final List<Reservation> reservationList = new ArrayList<>();

    @OneToOne(mappedBy = "customerMember")
    private CustomerAuth customerAuth;

    @OneToMany(mappedBy = "customerMember")   // Review의 customerMember 필드와 매핑
    private final List<Review> reviews = new ArrayList<>();

    public CustomerMember(String name, String phone, String carType, String carNumber) {
        this.name = name;
        this.phone = phone;
        this.carType = carType;
        this.carNumber = carNumber;
    }

    public void updateProfile(CustomerMemberProfileUpdateRequestDto dto) {
        if (dto.getName() != null){
            this.name = dto.getName();
        }
        if (dto.getPhone() != null){
            this.phone = dto.getPhone();
        }
        if (dto.getCarType() != null){
            this.carType = dto.getCarType();
        }
        if (dto.getCarNumber() != null){
            this.carNumber = dto.getCarNumber();
        }
    }

}
