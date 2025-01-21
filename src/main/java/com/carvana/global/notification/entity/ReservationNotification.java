package com.carvana.global.notification.entity;

import com.carvana.domain.customer.member.entity.CustomerMember;
import com.carvana.domain.reservation.entity.Reservation;
import com.carvana.domain.reservation.entity.ReservationStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("RESERVATION")
@SuperBuilder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservationNotification extends Notification {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @Override
    public String generateMessage() {
        if (reservation.getStatus() == ReservationStatus.PENDING) {
            return String.format("%s 예약이 신청되었습니다. 곧 확인 후 연락드리겠습니다.",
                reservation.getCarWash().getName());
        } else if (reservation.getStatus() == ReservationStatus.CONFIRMED) {
            return String.format("%s 예약이 수락되었습니다.",
                reservation.getCarWash().getName());
        } else if (reservation.getStatus() == ReservationStatus.DENIED) {
            return String.format("%s 예약이 거절되었습니다. 사유: %s",
                reservation.getCarWash().getName(),
                reservation.getRejectReason());
        } else if (reservation.getStatus() == ReservationStatus.COMPLETED) {
            return String.format("%s 세차가 완료되었습니다.",
                reservation.getCarWash().getName());
        }
        return getMessage();
    }

}
