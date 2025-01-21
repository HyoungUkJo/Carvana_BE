package com.carvana.global.notification.service;

import com.carvana.domain.customer.member.entity.CustomerMember;
import com.carvana.domain.reservation.entity.Reservation;
import com.carvana.global.notification.dto.NotificationResponseDto;
import com.carvana.global.notification.entity.Notification;
import com.carvana.global.notification.entity.ReservationNotification;
import com.carvana.global.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationService {
    private final NotificationRepository notificationRepository;

    // fcm push 알림 의존성 추가
    private final CustomerFcmService customerFcmService;

    // 예약 관련 알림 생성
    public void createReservationNotification(CustomerMember customer, Reservation reservation) {
        String title = "예약 상태 알림";
        String message = "";        // 자동 생성 예정

        ReservationNotification notification = ReservationNotification.builder()
            .customerMember(customer)
            .reservation(reservation)
            .title(title)
            .message(message)
            .isRead(false)
            .createdAt(LocalDateTime.now())
            .build();
        message = notification.generateMessage();  // 생성된 알림에서 메시지 생성
        notification.updateMessage(message);             // 메시지 업데이트
        notificationRepository.save(notification);

        // push 알림 발송
        customerFcmService.sendReservationNotification(customer.getId(), notification);

    }

    // 고객의 모든 알림 조회
    @Transactional(readOnly = true)
    public List<NotificationResponseDto> getCustomerNotifications(Long customerId) {
        // 사용자 검증 알고리즘 추가
        // CustomerMember customer = ...

        List<Notification> notifications = notificationRepository.findByCustomerMemberIdOrderByCreatedAtDesc(customerId);

        return notifications.stream()
            .map(NotificationResponseDto::new)
            .toList();
    }

    //

}
