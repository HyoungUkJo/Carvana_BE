package com.carvana.domain.owner.member.dto;

import com.carvana.domain.store.carwash.entity.CarWash;

import java.util.ArrayList;
import java.util.List;

public class OwnerProfileResponseDto {
    private Long id;
    private String name;
    private String phone;
    private List<CarWash> carWashes;

}
