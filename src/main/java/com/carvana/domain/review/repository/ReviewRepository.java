package com.carvana.domain.review.repository;

import com.carvana.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    // 리뷰 일자 정렬
    List<Review> findByCarWashIdOrderByCreatedAtDesc(Long carWashId);

    // 추후 페이지네이션 적용 (프론트와 합의 필요)
}
