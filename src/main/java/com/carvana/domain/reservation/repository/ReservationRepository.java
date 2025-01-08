package com.carvana.domain.reservation.repository;

import com.carvana.domain.reservation.entity.Reservation;
import com.carvana.domain.reservation.entity.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByCustomerMemberId(Long customerMemberId);

    // 특정 예약 상태의 예약을 조회
    List<Reservation> findByCarWashIdAndStatusOrderByCreateAtDesc(Long carWashId, ReservationStatus status);
}
