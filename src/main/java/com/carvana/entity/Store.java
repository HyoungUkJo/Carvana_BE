package com.carvana.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "stores")
@Getter
@NoArgsConstructor
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;        // 세차장 이름

    @Column(nullable = false, unique = true)    // 여기에 나온 에러를 컨트롤 해야함
    private String email;       // 로그인 이메일

    @Column(nullable = false)
    private String password;     // 패스워드

    @Column(name = "business_number", nullable = false, unique = true)
    private String businessNumber;   // 사업자 등록 번호

    @Column(nullable = false)
    private String address;          // 주소

    @Column(name = "phone_number",nullable = false)
    private String phoneNumber;     // 연락처

    @Column(name = "created_at")
    private LocalDateTime createdAt; //생성 시간

    @Column(name = "updated_at")
    private LocalDateTime updatedAt; // 수정 시간

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt; // 삭제 시간

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    // 자동 실행 어노테이션
    @PrePersist // 생성 전 실행
    protected void onCreate() {
        createdAt = LocalDateTime.now();    // 생성 시간 설정
        updatedAt = createdAt;
    }

    @PreUpdate  // 수정될때 실행
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @PreRemove  // 삭제될 때 실행
    protected void onDelete() {
        deleted = true;
        deletedAt = LocalDateTime.now();
    }

}
