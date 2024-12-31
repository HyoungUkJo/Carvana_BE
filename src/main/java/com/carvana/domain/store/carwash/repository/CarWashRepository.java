package com.carvana.domain.store.carwash.repository;

import com.carvana.domain.store.carwash.entity.CarWash;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarWashRepository extends JpaRepository<CarWash, Long> {
}
