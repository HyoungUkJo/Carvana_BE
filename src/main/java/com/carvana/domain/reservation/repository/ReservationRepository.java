package com.carvana.domain.reservation.repository;

import com.carvana.domain.reservation.entity.Reservation;
import com.carvana.domain.reservation.entity.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByCustomerMemberId(Long customerMemberId);

    // 특정 예약 상태의 예약을 조회
    List<Reservation> findByCarWashIdAndStatusOrderByCreateAtDesc(Long carWashId, ReservationStatus status);

    // 특정 기간과 특정 예약상태의 예약을 조회
    List<Reservation> findByCarWashIdAndStatusAndReservationDateTimeBetweenOrderByReservationDateTime(
        Long carWashId,
        ReservationStatus status,
        LocalDateTime startOfMonth,
        LocalDateTime endOfMonth
    );

    // 특정 기간의 예약을 조회
    List<Reservation> findByCarWashIdAndReservationDateTimeBetweenOrderByReservationDateTime(
        Long carWashId,
        LocalDateTime startOfMonth,
        LocalDateTime endOfMonth
    );

    // 특정 기간의 특정 예약 상태들의 예약 조회
    List<Reservation> findByCarWashIdAndReservationDateTimeBetweenAndStatusIn(
        Long carWashId,
        LocalDateTime startDate,
        LocalDateTime endDate,
        List<ReservationStatus> statuses
    );

    // 특정 베이의 특정 상태의 예약 조회
    List<Reservation> findByCarWashIdAndStatusAndBayNumberAndReservationDateTimeBetweenOrderByReservationDateTime(
        Long carWashId,
        ReservationStatus status,
        Integer bayNumber,
        LocalDateTime startDateTime,
        LocalDateTime endDateTime
    );


}
