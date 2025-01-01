package com.carvana.domain.reservation.repository;

import com.carvana.domain.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByCustomerMemberId(Long customerMemberId);
}
