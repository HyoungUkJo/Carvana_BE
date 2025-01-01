package com.carvana.domain.store.carwash.repository;

import com.carvana.domain.store.carwash.entity.CarWash;
import com.carvana.domain.store.carwash.entity.CarWashMenu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarWashMenuRepository extends JpaRepository<CarWashMenu, Long> {
}
