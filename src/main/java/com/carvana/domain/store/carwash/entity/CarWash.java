package com.carvana.domain.store.carwash.entity;

import com.carvana.domain.owner.member.entity.OwnerMember;
import com.carvana.domain.store.carwash.dto.CarWashProfileUpdateRequestDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class CarWash {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;        // 세차장 이름
    private String address;     // 주소
    private String phone;       // 연락처
    private String businessHours;   // 영업 시간

    private Integer bayCount;   // 베이 수
    private String thumbnailImgUrl; // 대표 이미지(썸네일)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_member_id")
    private OwnerMember ownerMember;

    @OneToMany(mappedBy = "carWash", cascade = CascadeType.ALL)
    private List<CarWashMenu> menus = new ArrayList<>();

    @Builder
    public CarWash(Long id, String name, String address, String phone, String businessHours, Integer bayCount, String thumbnailImgUrl) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.businessHours = businessHours;
        this.bayCount = bayCount;
        this.thumbnailImgUrl = thumbnailImgUrl;
    }

    public void setOwnerMember(OwnerMember ownerMember) {
        this.ownerMember = ownerMember;
    }

    public void addMenu(CarWashMenu menu) {
        this.menus.add(menu);
        menu.setCarWash(this);
    }

    // 프로필 수정
    public void updateCarWash(CarWashProfileUpdateRequestDto dto) {
        if (dto.getName() != null) {
            this.name = dto.getName();
        }
        if (dto.getAddress() != null) {
            this.address = dto.getAddress();
        }
        if (dto.getPhone() != null) {
            this.phone = dto.getPhone();
        }
        if (dto.getBusinessHours() != null) {
            this.businessHours = dto.getBusinessHours();
        }
        if (dto.getBayCount() != null) {
            this.bayCount=dto.getBayCount();
        }
        if (dto.getBusinessNumber() != null) {
            this.ownerMember.setBusinessNumber(dto.getBusinessNumber());
        }
        if (dto.getThumbnailImgUrl() != null) {
            this.thumbnailImgUrl = dto.getThumbnailImgUrl();
        }
    }
}
