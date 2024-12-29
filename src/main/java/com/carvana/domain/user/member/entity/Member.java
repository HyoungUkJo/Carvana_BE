package com.carvana.domain.user.member.entity;

import com.carvana.domain.user.auth.entity.Auth;
import com.carvana.domain.user.reservation.entity.Reservation;
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
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String phone;

    @OneToMany(mappedBy = "member")
    private final List<Reservation> reservationList = new ArrayList<>();

    @OneToOne(mappedBy = "member")
    private Auth auth;

    public Member(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }
}
