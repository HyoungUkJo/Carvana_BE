package com.carvana.global.notification.dto;

import com.carvana.global.notification.entity.Notification;
import com.carvana.global.notification.entity.ReservationNotification;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Getter
@NoArgsConstructor
public class NotificationResponseDto {
    private Long id;
    private String title;
    private String message;
    private boolean isRead;
    private LocalDateTime createdAt;
    private String notificationType;    // RESERVATION, EVENT 등 알림 타입
    private Map<String, Object> data;   // 알림 타입별 추가 데이터

    @Builder
    public NotificationResponseDto(Notification notification) {
        this.id = notification.getId();
        this.title = notification.getTitle();
        this.message = notification.getMessage();
        this.isRead = notification.isRead();
        this.createdAt = notification.getCreatedAt();
        this.data = new HashMap<>();

        if (notification instanceof ReservationNotification) {
            ReservationNotification reservationNotification = (ReservationNotification) notification;
            this.notificationType = "RESERVATION";
            this.data.put("reservationId", reservationNotification.getReservation().getId());
            this.data.put("carWashName", reservationNotification.getReservation().getCarWash().getName());
            this.data.put("reservationStatus", reservationNotification.getReservation().getStatus());
        }
        // else if 로 추후 다른 알림이 추가되었을때 사용
    }
}
