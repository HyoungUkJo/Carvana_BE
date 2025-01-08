package com.carvana.domain.review.service;

import com.carvana.domain.review.dto.ReviewResponseDto;
import com.carvana.domain.review.entity.Review;
import com.carvana.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    //세차장 리뷰 전체 읽어오는 서비스 -> 추후 페이지네이션으로
    public List<ReviewResponseDto> getAllReviews(Long carWashId) {
        List<Review> reviews = reviewRepository.findByCarWashIdOrderByCreatedAtDesc(carWashId);

        return reviews.stream()
            .map(review -> ReviewResponseDto.builder()
            .customerName(review.getCustomerMember().getName())
            .rating(review.getRating())
            .content(review.getContent())
            .createdAt(review.getCreatedAt())
            .imageUrls(review.getImageUrls())
            .build())
            .collect(Collectors.toList());
    }

}
