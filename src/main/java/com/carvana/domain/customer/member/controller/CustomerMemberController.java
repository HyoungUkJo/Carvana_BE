package com.carvana.domain.customer.member.controller;

import com.carvana.domain.customer.member.dto.CustomerMemberProfileResponseDto;
import com.carvana.domain.customer.member.dto.CustomerMemberProfileUpdateRequestDto;
import com.carvana.domain.customer.member.dto.CustomerMyPageResponseDto;
import com.carvana.domain.customer.member.dto.MyReservationResponseDto;
import com.carvana.domain.customer.member.service.CustomerMemberService;
import com.carvana.domain.reservation.dto.ReservationResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerMemberController {
    private final CustomerMemberService customerMemberService;

    // 내 프로필 요청
    @GetMapping("/{memberId}/myProfile")
    public CustomerMemberProfileResponseDto getMyProfile(@PathVariable Long memberId) {
        return customerMemberService.getMyProfile(memberId);
    }

    // 마이페이지
    @GetMapping("/{memberId}/myPage")
    public CustomerMyPageResponseDto getMyPage(@PathVariable Long memberId) {
        return customerMemberService.getMyPage(memberId);
    }

    // 내 프로필 수정
    @PostMapping("{memberId}/updateProfile")
    public CustomerMemberProfileResponseDto updateMyProfile(@PathVariable Long memberId, @RequestBody CustomerMemberProfileUpdateRequestDto updateRequestDto){
        return customerMemberService.updateMyProfile(memberId, updateRequestDto);
    }

    // 내 예약 기록
    @GetMapping("/{memberId}/myReservation")
    public List<MyReservationResponseDto> getMyReservation(@PathVariable Long memberId) {
        return customerMemberService.getMyReservation(memberId);
    }

}
