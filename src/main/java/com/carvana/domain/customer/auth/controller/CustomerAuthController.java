package com.carvana.domain.customer.auth.controller;

import com.carvana.domain.customer.auth.dto.SignInRequestDto;
import com.carvana.domain.customer.auth.dto.SignInResponseDto;
import com.carvana.domain.customer.auth.dto.SignUpResponseDto;
import com.carvana.domain.customer.auth.service.CustomerAuthService;
import com.carvana.domain.customer.auth.dto.SignUpRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/auth")
public class CustomerAuthController {

    private final CustomerAuthService customerAuthService;
    @PostMapping("/signup")
    public SignUpResponseDto signUp(@RequestBody SignUpRequestDto signUpRequestDto) {
        return customerAuthService. signUp(signUpRequestDto);
    }

    @PostMapping("/signin")
    public SignInResponseDto signIn(@RequestBody SignInRequestDto signInRequestDto) {
        return customerAuthService.signIn(signInRequestDto);
    }
}
