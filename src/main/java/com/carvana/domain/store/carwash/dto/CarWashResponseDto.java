package com.carvana.domain.store.carwash.dto;

import com.carvana.domain.store.carwash.entity.CarWash;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CarWashResponseDto {
    private Long id;
    private String name;
    private String address;       // full address
    private String region;
    private String district;
    private String thumbnailImgKey;
    private Double rating;
    private Integer minPrice;
    private Integer maxPrice;

    public static CarWashResponseDto from(CarWash carWash) {
        return CarWashResponseDto.builder()
            .id(carWash.getId())
            .name(carWash.getName())
            .address(carWash.getAddress())
            .region(carWash.getRegion())
            .district(carWash.getDistrict())
            .thumbnailImgKey(carWash.getThumbnailImgKey())
            .rating(carWash.getRating())
            .minPrice(carWash.getMinPrice())
            .maxPrice(carWash.getMaxPrice())
            .build();
    }
}
