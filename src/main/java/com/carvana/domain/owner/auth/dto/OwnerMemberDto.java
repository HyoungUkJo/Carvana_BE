package com.carvana.domain.owner.auth.dto;

import com.carvana.domain.store.carwash.entity.CarWash;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class OwnerMemberDto {
    private String name;

    private String phone;

    private String businessNumber; //사업자 번호

    private List<CarWash> carWashesList;   // 업장 [여러개일 경우 대비]

    private String address; //  주소
}
