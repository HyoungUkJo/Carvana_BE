package com.carvana.domain.owner.auth.controller;

import com.carvana.domain.owner.auth.dto.SignInRequestDto;
import com.carvana.domain.owner.auth.dto.SignInResponseDto;
import com.carvana.domain.owner.auth.dto.SignUpRequestDto;
import com.carvana.domain.owner.auth.dto.SignUpResponseDto;
import com.carvana.domain.owner.auth.service.OwnerAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
