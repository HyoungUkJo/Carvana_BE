package com.carvana.domain.reservation.entity;

import com.carvana.domain.costomer.member.entity.CustomerMember;
import com.carvana.domain.owner.member.entity.OwnerMember;
import com.carvana.domain.store.carwash.entity.CarWash;
import com.carvana.domain.store.carwash.entity.CarWashMenu;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)  // member가 필요할때만 사용할 수 있도록 Lazy하게 설정
    @JoinColumn(name = "customer_member_id")
    private CustomerMember customerMember;
//
//    @ManyToOne(fetch = FetchType.LAZY)  // member가 필요할때만 사용할 수 있도록 Lazy하게 설정
//    @JoinColumn(name = "owner_member_id")
//    private OwnerMember ownerMember;


    @ManyToOne(fetch = FetchType.LAZY)  // member가 필요할때만 사용할 수 있도록 Lazy하게 설정
    @JoinColumn(name = "carwash_id")
    private CarWash carWash;            // 세차장 엔티티

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private CarWashMenu menu;           // 세차 메뉴 엔티티

    private LocalDateTime reservationDateTime;  // 예약 시간

    private String request; // 요청사항

    private String imgUrl;  // 이미지 저장된 Url Todo: S3에 연결 지금은 무료로 연결필요

    @Enumerated(EnumType.STRING)
    private ReservationStatus status = ReservationStatus.PENDING;

    private LocalDateTime createAt;   // 예약 요청 시간

    @Builder

    public Reservation(Long id, CustomerMember customerMember, CarWash carWash,
                       CarWashMenu menu, LocalDateTime reservationDateTime,
                       String request, String imgUrl, LocalDateTime createAt) {
        this.id = id;
        this.customerMember = customerMember;
        this.carWash = carWash;
        this.menu = menu;
        this.reservationDateTime = reservationDateTime;
        this.request = request;
        this.imgUrl = imgUrl;
        this.createAt = createAt;
    }
}
