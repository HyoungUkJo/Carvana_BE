package com.carvana.domain.store.carwash.repository;

import com.carvana.domain.store.carwash.entity.CarWashBusinessTarget;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarWashBusinessTargetRepository extends JpaRepository<CarWashBusinessTarget,Long> {
    // CarWash 아이디로 목표치 가지고오기
    Optional<CarWashBusinessTarget> findByCarWashId(Long carWashId);
}
