package com.carvana.domain.store.carwash.repository;

import com.carvana.domain.store.carwash.entity.CarWash;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarWashRepository extends JpaRepository<CarWash, Long> {

    // 이름에 특정 키워드가 포함된 세차장 찾기
    List<CarWash> findByNameContainingIgnoreCase(String keyword);

}
