package com.carvana.domain.store.carwash.repository;

import com.carvana.domain.store.carwash.entity.CarWash;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CarWashRepository extends JpaRepository<CarWash, Long> {

    // 이름에 특정 키워드가 포함된 세차장 찾기
    List<CarWash> findByNameContainingIgnoreCase(String keyword);

    // 세차장 검색용 커스텀 쿼리
    @Query("SELECT DISTINCT cw FROM CarWash cw " +
        "JOIN cw.menus m " +
        "WHERE (:name IS NULL OR cw.name LIKE %:name%) " +
        "AND (:region IS NULL OR cw.region = :region) " +
        "AND (:district IS NULL OR cw.district = :district) " +
        "AND (:menuName IS NULL OR m.menuName = :menuName) " +
        "AND (:minPrice IS NULL OR m.price >= :minPrice) " +
        "AND (:maxPrice IS NULL OR m.price <= :maxPrice)")
    List<CarWash> search(
        @Param("name") String name,
        @Param("region") String region,
        @Param("district") String district,
        @Param("menuName") String menuName,
        @Param("minPrice") Integer minPrice,
        @Param("maxPrice") Integer maxPrice
    );
}
