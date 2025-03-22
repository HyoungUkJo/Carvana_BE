package com.carvana.domain.owner.auth.entity;

import com.carvana.domain.owner.member.entity.OwnerMember;
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

    private String address;

    private String typeOfBusiness;

    // jwt 리프레시 토큰
//    private String refreshToken;

    // Todo: 소셜 로그인 설정

    // 멤버 연관관계 설정
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_member_id")
    private OwnerMember ownerMember;

    @Builder
    public OwnerAuth(String email,
                     String password,
                     OwnerMember ownerMember,
                     String address,
                     String typeOfBusiness) {
        this.email = email;
        this.password = password;
        this.ownerMember = ownerMember;
        this.address = address;
        this.typeOfBusiness = typeOfBusiness;
    }

/*    public void updateRefreshToken(String refreshToken) {
       this.refreshToken = refreshToken;
    }*/


}
