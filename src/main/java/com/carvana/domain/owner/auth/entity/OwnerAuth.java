package com.carvana.domain.owner.auth.entity;

import com.carvana.domain.owner.member.entity.OwnerMember;
import com.carvana.domain.store.carwash.entity.CarWash;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class OwnerAuth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;

    // jwt 리프레시 토큰
//    private String refreshToken;

    // Todo: 소셜 로그인 설정

    // fcm 토큰 ( push알림을 위한 토큰)
    private String fcmToken;

    // 멤버 연관관계 설정
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_member_id")
    private OwnerMember ownerMember;

    @Builder
    public OwnerAuth(String email, String password, OwnerMember ownerMember) {
        this.email = email;
        this.password = password;
        this.ownerMember = ownerMember;
    }

    // FcmToken을 업데이트 하기위한 메소드
    public void updateFcmToken(String fcmToken){
        this.fcmToken = fcmToken;
    }
/*    public void updateRefreshToken(String refreshToken) {
       this.refreshToken = refreshToken;
    }*/


}
