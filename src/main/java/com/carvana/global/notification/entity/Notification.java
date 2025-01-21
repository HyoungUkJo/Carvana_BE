package com.carvana.global.notification.entity;

import com.carvana.domain.customer.member.entity.CustomerMember;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Getter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)  // 한 테이블에 모든 자식 클래스 데이터를 저장
@DiscriminatorColumn(name = "notification_type")  // 구분 컬럼
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public abstract class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_member_id")
    private CustomerMember customerMember;

    private String title;       // 알림 제목

    private String message;     // 알림 내용

    private boolean isRead;     // 읽음 여부

    private LocalDateTime createdAt;    // 생성일자

    public Notification(CustomerMember customerMember, String title, String message) {
        this.customerMember = customerMember;
        this.title = title;
        this.message = message;
        this.isRead = false;
        this.createdAt = LocalDateTime.now();
    }

    // 메시지를 수정하는 메서드
    public void updateMessage(String message) {
        this.message = message;
    }


    public boolean isRead() {
        return isRead;
    }

    // 메시지 생성 추상함수 생성
    public abstract String generateMessage();
}
