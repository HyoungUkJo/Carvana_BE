package com.carvana.domain.review.controller;

import com.carvana.domain.review.dto.ReviewResponseDto;
import com.carvana.domain.review.repository.ReviewRepository;
import com.carvana.domain.review.service.ReviewService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "리뷰", description = "리뷰와 관련된 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/review")
public class ReviewController {
    private final ReviewRepository reviewRepository;
    private final ReviewService reviewService;

    // 전체 리뷰 return
    @GetMapping("/getAllReviews/{carWashId}")
    public List<ReviewResponseDto> getAllReviews(@PathVariable Long carWashId){
        return reviewService.getAllReviews(carWashId);
    }
}
