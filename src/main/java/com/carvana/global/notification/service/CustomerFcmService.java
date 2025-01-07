package com.carvana.global.notification.service;

import com.carvana.domain.customer.member.entity.CustomerMember;
import com.carvana.domain.customer.member.repository.CustomerMemberRepository;
import com.carvana.domain.reservation.entity.Reservation;
import com.carvana.global.exception.custom.FCMException;
import com.carvana.global.notification.entity.CustomerFcmToken;
import com.carvana.global.notification.repository.CustomerFcmTokenRepository;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j  // 로깅을 위해
public class CustomerFcmService {
    private final FirebaseMessaging firebaseMessaging;
    private final CustomerFcmTokenRepository customerFcmTokenRepository;
    private final CustomerMemberRepository customerMemberRepository;

    // 토큰 생성 메소드
    private CustomerFcmToken createCustomerFcmToken(Long customerId, String token) {
        CustomerMember customerMember = customerMemberRepository.findById(customerId)
            .orElseThrow(() -> new IllegalStateException("존재하지 않는 사용자입니다."));

        CustomerFcmToken fcmToken = CustomerFcmToken.builder()
            .token(token)
            .customerMember(customerMember)
            .build();

        return customerFcmTokenRepository.save(fcmToken);
    }

    //토큰 저장/ 갱신 -> 범용적으로 사용할 수 있도록 customer을 제거할 수 있을것같음
    @Transactional
    public void saveCustomerToken(Long customerId, String token) {
        // customer 검증
        CustomerMember customerMember = customerMemberRepository.findById(customerId)
            .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 사용자"));

        // 토큰 유무 확인 없다면 엔티티 생성
        CustomerFcmToken customerFcmToken = customerFcmTokenRepository.findByCustomerMemberId(customerId)
            .orElseGet(() -> createCustomerFcmToken(customerId, token));

        // 토큰 엔티티에 토큰이 없거나 같지 않으면 받은걸로 갱신
        if (customerFcmToken.getToken() != null && !customerFcmToken.getToken().equals(token)) {
            customerFcmToken.updateToken(token);
        }
    }

    // 알림 보내기
    public void sendNewReservationNotification(Long customerId, Reservation reservation) {
        CustomerFcmToken customerFcmToken = customerFcmTokenRepository.findByCustomerMemberId(customerId)
            .orElseThrow(() -> new IllegalStateException("FCM토큰이 없습니다."));

        Message message = Message.builder()
            .setToken(customerFcmToken.getToken())
            .setNotification(Notification.builder()
                .setTitle("새로운 예약 요청")
                .setBody(String.format("%s님이 %s 메뉴를 예약 했습니다.",
                    reservation.getCustomerMember().getName(),
                    reservation.getMenu().getMenuName()))
                .build())
            .build();

        try {
            firebaseMessaging.send(message);
            log.info("Fcm발송 성공");
        } catch (FirebaseMessagingException e){
            log.error("FCM 발송 실패: {}", e.getMessage(),e);
            throw new FCMException("FCM 발송에 실패했습니다.");
        }
    }
}
