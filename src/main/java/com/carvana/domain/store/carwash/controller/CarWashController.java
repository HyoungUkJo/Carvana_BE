package com.carvana.domain.store.carwash.controller;

import com.carvana.domain.store.carwash.dto.RegisterCarWashMenuRequestDto;
import com.carvana.domain.store.carwash.dto.RegisterCarWashMenuResponseDto;
import com.carvana.domain.store.carwash.dto.RegisterCarWashRequestDto;
import com.carvana.domain.store.carwash.dto.RegisterCarWashResponseDto;
import com.carvana.domain.store.carwash.service.CarWashService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/carwash")
@RequiredArgsConstructor
public class CarWashController {

    private final CarWashService carWashService;

    @PostMapping("/register/CarWash")
    public RegisterCarWashResponseDto registerCarWash(@RequestBody RegisterCarWashRequestDto registerCarWashRequestDto) {
        // 토큰이 있다면 여기서 사용자 인증을 해야함
        // 테스트로 쓰는것임으로 일단은 사장 Id를 따로 받는것으로 테스트
        // TODO: 추후 토큰으로 확인할 수 있도록 Auth도 의존관계 설정
        //

        Long ownerId = 1L;
        return carWashService.registerCarWash(registerCarWashRequestDto, ownerId);
    }

    @PostMapping("/{carWashId}/register/menues")
    public RegisterCarWashMenuResponseDto registerCarWashMenu(@RequestBody RegisterCarWashMenuRequestDto registerCarWashMenuRequestDto) {
        Long carWashId = 1L;
        return carWashService.registerCarWashMenu(carWashId, registerCarWashMenuRequestDto);
    }

}
