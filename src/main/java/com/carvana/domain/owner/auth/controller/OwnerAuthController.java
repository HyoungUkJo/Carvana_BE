package com.carvana.domain.owner.auth.controller;

import com.carvana.domain.owner.auth.dto.*;
import com.carvana.domain.owner.auth.service.OwnerAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/owner/auth")
public class OwnerAuthController {

    private final OwnerAuthService ownerAuthService;
    @PostMapping("/signup")
    public SignUpResponseDto signUp(@RequestBody SignUpRequestDto signUpRequestDto) {
        return ownerAuthService.signUp(signUpRequestDto);
    }

    @PostMapping("/signin")
    public SignInResponseDto signIn(@RequestBody SignInRequestDto signInRequestDto) {
        return ownerAuthService.signIn(signInRequestDto);
    }

    @GetMapping("/me")
    public OwnerMemberDto getCurrentUserInfo() {
        return ownerAuthService.getCurrentUserInfo();
    }
}
