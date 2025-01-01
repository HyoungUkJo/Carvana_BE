package com.carvana.domain.reservation.entity;

public enum ReservationStatus {
    PENDING,    // 대기중 (초기 상태)
    DENIED,     // 예약 거절
    CONFIRMED,  // 예약 확정
    CANCELLED,  // 예약 취소
    COMPLETED   // 서비스 완료
}
