package com.carvana.domain.store.carwash.controller;

import com.carvana.domain.store.carwash.dto.*;
import com.carvana.domain.store.carwash.service.CarWashService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

        return carWashService.registerCarWash(registerCarWashRequestDto);
    }

    @PostMapping("/{carWashId}/register/menues")
    public RegisterCarWashMenuResponseDto registerCarWashMenu(@PathVariable Long carWashId, @RequestBody RegisterCarWashMenuRequestDto registerCarWashMenuRequestDto) {
        return carWashService.registerCarWashMenu(carWashId, registerCarWashMenuRequestDto);
    }

    // 세차장 프로필 요청
    @GetMapping("/{carWashId}/profile")
    public CarWashProfileResponseDto getCarWashProfile(@PathVariable Long carWashId) {
        return carWashService.getCarWashProfile(carWashId);
    }

    // 세차장 프로필 수정
    @PostMapping("/{carWashId}/profile/update")
    public CarWashProfileResponseDto updateCarWashProfile(@PathVariable Long carWashId, @RequestBody CarWashProfileUpdateRequestDto updateRequestDto) {
        return carWashService.updateCarWashProfile(carWashId, updateRequestDto);
    }


}
