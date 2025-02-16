package com.carvana.domain.reservation.entity;

import com.carvana.domain.customer.member.entity.CustomerMember;
import com.carvana.domain.reservation.dto.ReservationUpdateRequestDto;
import com.carvana.domain.store.carwash.entity.CarWash;
import com.carvana.domain.store.carwash.entity.CarWashMenu;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String uuid;    // 예약을 식별할 uuid

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

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)   // 연쇄 반응 설정 cascade, orphanremoval
    private final List<ReservationMenu> reservationMenus = new ArrayList<>();       // 선택한 세차 메뉴

    private String carType;             // 세차할 차종

    private String carNumber;           // 세차할 차번호

    private LocalDateTime reservationDateTime;  // 예약 시간

    private String request; // 요청사항

    @ElementCollection
    @CollectionTable(name = "reservation_image_keys",
        joinColumns = @JoinColumn(name = "reservation_id"))
    @Column(name = "image_key")
    private List<String> imageKeys = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private ReservationStatus status = ReservationStatus.PENDING;

    private String rejectReason;       // 예약 거절 사유

    private LocalDateTime createAt;   // 예약 요청 시간

    private Integer bayNumber;          // 예약 베이 번호

    @Builder
    public Reservation(Long id,
                       CustomerMember customerMember,
                       CarWash carWash,
                       String uuid,
                       String carType,
                       String carNumber,
                       LocalDateTime reservationDateTime,
                       String request,
                       List<String> imageKeys,
                       LocalDateTime createAt,
                       Integer bayNumber
    ) {
        this.id = id;
        this.customerMember = customerMember;
        this.carWash = carWash;
        this.uuid = uuid;
        this.carType = carType;
        this.carNumber = carNumber;
        this.reservationDateTime = reservationDateTime;
        this.request = request;
        this.imageKeys = imageKeys;
        this.createAt = createAt;
        this.bayNumber = bayNumber;
    }

    public void updateStatus(ReservationUpdateRequestDto requestDto) {
        this.status = requestDto.getStatus();
        this.rejectReason = requestDto.getRejectReason();
    }

    // 예약 메뉴 추가 메서드
    public void addMenu(CarWashMenu carWashMenu) {
        ReservationMenu reservationMenu = ReservationMenu.builder()
            .reservation(this)
            .carWashMenu(carWashMenu)
            .build();
        this.reservationMenus.add(reservationMenu);
    }

    // 세차 가격 계산 메서드
    public int calculateTotalPrice() {
        return reservationMenus.stream()
            .mapToInt(reservationMenus -> reservationMenus.getCarWashMenu().getPrice()).sum();
    }

    // 세차 소요 시간 계산 메서드
    public int calculateTotalDuration() {
        return reservationMenus.stream()
            .mapToInt(reservationMenus -> reservationMenus.getCarWashMenu().getDuration()).sum();
    }

    // 선택한 메뉴를 가지고오는 메서드
    public List<String> getMenuNameList() {
        return reservationMenus.stream()
            .map(reservationMenu -> reservationMenu.getCarWashMenu().getMenuName())
            .toList();
    }

}
