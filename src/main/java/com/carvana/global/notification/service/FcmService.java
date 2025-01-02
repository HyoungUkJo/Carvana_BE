package com.carvana.global.notification.service;

import com.carvana.domain.owner.member.entity.OwnerMember;
import com.carvana.domain.owner.member.repository.OwnerMemberRepository;
import com.carvana.domain.reservation.entity.Reservation;
import com.carvana.global.notification.entity.OwnerFcmToken;
import com.carvana.global.notification.repository.CustomerFcmTokenRepository;
import com.carvana.global.notification.repository.OwnerFcmTokenRepository;
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
public class FcmService {
    private final FirebaseMessaging firebaseMessaging;
    private final OwnerFcmTokenRepository ownerFcmTokenRepository;
    private final CustomerFcmTokenRepository customerFcmTokenRepository;
    private final OwnerMemberRepository ownerMemberRepository;

    // 토큰 생성 메소드
    private OwnerFcmToken createOwnerFcmToken(Long ownerId, String token) {
        OwnerMember ownerMember = ownerMemberRepository.findById(ownerId)
            .orElseThrow(() -> new IllegalStateException("존재하지 않는 사용자입니다."));

        OwnerFcmToken fcmToken = OwnerFcmToken.builder()
            .token(token)
            .ownerMember(ownerMember)
            .build();

        return ownerFcmTokenRepository.save(fcmToken);
    }

    //토큰 저장/ 갱신 -> 범용적으로 사용할 수 있도록 Owner을 제거할 수 있을것같음
    @Transactional
    public void saveOwnerToken(Long ownerId, String token) {
        // owner 검증
        OwnerMember ownerMember = ownerMemberRepository.findById(ownerId)
            .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 사용자"));

        // 토큰 유무 확인 없다면 엔티티 생성
        OwnerFcmToken ownerFcmToken = ownerFcmTokenRepository.findByOwnerMemberId(ownerId)
            .orElseGet(() -> createOwnerFcmToken(ownerId, token));

        // 토큰 엔티티에 토큰이 없거나 같지 않으면 받은걸로 갱신
        if (ownerFcmToken.getToken() != null && !ownerFcmToken.getToken().equals(token)) {
            ownerFcmToken.updateToken(token);
        }
    }

    // 알림 보내기
    public void sendNewReservationNotification(Long ownerId, Reservation reservation) {
        OwnerFcmToken ownerFcmToken = ownerFcmTokenRepository.findByOwnerMemberId(ownerId)
            .orElseThrow(() -> new IllegalStateException("FCM토큰이 없습니다."));

        Message message = Message.builder()
            .setToken(ownerFcmToken.getToken())
            .setNotification(Notification.builder()
                .setTitle("새로운 예약 요청")
                .setBody(String.format("%s님이 %s 메뉴를 예약 했습니다.",
                    reservation.getCustomerMember().getName(),
                    reservation.getMenu().getMenuName()))
                .build())
            .build();

        try {
            firebaseMessaging.send(message);
        } catch (FirebaseMessagingException e){
            log.error("FCM 발송 실패: {}", e.getMessage(),e);
        }
    }
}
