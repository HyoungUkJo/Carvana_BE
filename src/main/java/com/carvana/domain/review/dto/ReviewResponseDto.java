package com.carvana.domain.review.dto;

import com.carvana.domain.customer.member.entity.CustomerMember;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class ReviewResponseDto {
    private String customerName;
    private int rating;
    private String content;
    private List<String> imageUrls = new ArrayList<>();
    private LocalDateTime createdAt;

    @Builder
    public ReviewResponseDto(String customerName, int rating, String content,
                             List<String> imageUrls, LocalDateTime createdAt) {
        this.customerName = customerName;
        this.rating = rating;
        this.content = content;
        this.imageUrls = imageUrls;
        this.createdAt = createdAt;
    }
}
