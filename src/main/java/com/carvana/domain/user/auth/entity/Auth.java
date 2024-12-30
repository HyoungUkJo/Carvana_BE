package com.carvana.domain.user.auth.entity;

import com.carvana.domain.user.member.entity.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Auth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;

    // jwt 리프레시 토큰
//    private String refreshToken;

    // Todo: 소셜 로그인 설정


    // 멤버 연관관계 설정
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Auth(String email, String password, Member member) {
        this.email = email;
        this.password = password;
        this.member = member;
    }

/*    public void updateRefreshToken(String refreshToken) {
       this.refreshToken = refreshToken;
    }*/
}
