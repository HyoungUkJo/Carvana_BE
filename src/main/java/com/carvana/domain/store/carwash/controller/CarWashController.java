package com.carvana.domain.store.carwash.controller;

import com.carvana.domain.reservation.dto.MonthlyStatsDto;
import com.carvana.domain.store.carwash.dto.*;
import com.carvana.domain.store.carwash.service.CarWashService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@Tag(name = "세차장 관련 API", description = "세차장 등록 및 정보 수정 API")
@RestController
@RequestMapping("/api/carwash")
@RequiredArgsConstructor
public class CarWashController {

    private final CarWashService carWashService;

    @Operation(summary = "세차장등록", description = "세차장 등록하는 API")
    @PostMapping("/register/CarWash")
    public RegisterCarWashResponseDto registerCarWash(@RequestBody RegisterCarWashRequestDto registerCarWashRequestDto) {
        // 토큰이 있다면 여기서 사용자 인증을 해야함
        // 테스트로 쓰는것임으로 일단은 사장 Id를 따로 받는것으로 테스트
        // TODO: 추후 토큰으로 확인할 수 있도록 Auth도 의존관계 설정
        //

        return carWashService.registerCarWash(registerCarWashRequestDto);
    }

    @Operation(summary = "세차장메뉴 등록", description = "세차장 메뉴 등록하는 API 세차장이 먼저 등록되어있어야한다.")
    @PostMapping("/{carWashId}/register/menues")
    public RegisterCarWashMenuResponseDto registerCarWashMenu(@PathVariable Long carWashId, @RequestBody RegisterCarWashMenuRequestDto registerCarWashMenuRequestDto) {
        return carWashService.registerCarWashMenu(carWashId, registerCarWashMenuRequestDto);
    }

    // 세차장 프로필 요청
    @Operation(summary = "세차장 프로필 요청", description = "세차장 프로필 요청 API")
    @GetMapping("/{carWashId}/profile")
    public CarWashProfileResponseDto getCarWashProfile(@PathVariable Long carWashId) {
        return carWashService.getCarWashProfile(carWashId);
    }

    // 세차장 프로필 수정
    @Operation(summary = "세차장 프로필 수정", description = "세차장 프로필 수정 API")
    @PostMapping("/{carWashId}/profile/update")
    public CarWashProfileResponseDto updateCarWashProfile(@PathVariable Long carWashId, @RequestBody CarWashProfileUpdateRequestDto updateRequestDto) {
        return carWashService.updateCarWashProfile(carWashId, updateRequestDto);
    }

    // 월간 매출 작업량 리뷰량
    @Operation(summary = "월간 매출 작업량 리뷰량", description = "월간 매출 작업량 리뷰량 API")
    @GetMapping("/{carWashId}/stats/monthly")
    public CarWashMonthlyStatsDto getMonthlyStats(@PathVariable Long carWashId) {
        return carWashService.getMonthlyStats(carWashId);
    }

}
