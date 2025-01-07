package com.carvana.domain.customer.auth.entity;

import com.carvana.domain.customer.member.entity.CustomerMember;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class CustomerAuth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;

    // jwt 리프레시 토큰
//    private String refreshToken;

    // Todo: 소셜 로그인 설정

    // fcmToken (push알림을 위한 토큰)
    private String fcmToken;

    // 멤버 연관관계 설정
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_member_id")
    private CustomerMember customerMember;

    @Builder
    public CustomerAuth(String email, String password, CustomerMember customerMember) {
        this.email = email;
        this.password = password;
        this.customerMember = customerMember;
    }

    public void updateFcmToken(String fcmToken){
        this.fcmToken = fcmToken;
    }

/*    public void updateRefreshToken(String refreshToken) {
       this.refreshToken = refreshToken;
    }*/
}
