package com.carvana.domain.customer.auth.controller;

import com.carvana.domain.customer.auth.dto.*;
import com.carvana.domain.customer.auth.entity.CustomerAuth;
import com.carvana.domain.customer.auth.service.CustomerAuthService;
import com.carvana.domain.customer.member.entity.CustomerMember;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/auth")
public class CustomerAuthController {

    private final CustomerAuthService customerAuthService;
    @PostMapping("/signup")
    public SignUpResponseDto signUp(@RequestBody SignUpRequestDto signUpRequestDto) {
        return customerAuthService. signUp(signUpRequestDto);
    }
    @PostMapping("/email-exists")
    public EmailCheckResponseDto checkEmailDuplicate(@RequestBody @Valid EmailCheckRequestDto emailCheckRequestDto) {
        return customerAuthService.checkEmailDuplicate(emailCheckRequestDto.getEmail());
    }

    @PostMapping("/signin")
    public SignInResponseDto signIn(@RequestBody SignInRequestDto signInRequestDto) {
        return customerAuthService.signIn(signInRequestDto);
    }

    @GetMapping("/me")
    public CustomerMemberDto getCurrentUser() {
        return customerAuthService.getCurrentUserInfo();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String authHeader) {
        // Bearer 접두사 제거
        String token = authHeader.substring(7);

        // 토큰 블랙리스트에 추가하는 로직 (Redis 등 사용)
        // tokenBlacklistService.addToBlacklist(token, jwtTokenProvider.getExpirationTime(token));

        return ResponseEntity.ok().build();
    }
}
