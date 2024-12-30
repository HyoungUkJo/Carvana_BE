package com.carvana.domain.user.auth.controller;

import com.carvana.domain.user.auth.dto.SignInRequestDto;
import com.carvana.domain.user.auth.dto.SignInResponseDto;
import com.carvana.domain.user.auth.dto.SignUpResponseDto;
import com.carvana.domain.user.auth.service.AuthService;
import com.carvana.domain.user.auth.dto.SignUpRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/auth")
public class AuthController {

    private final AuthService authService;
    @PostMapping("/signup")
    public SignUpResponseDto signUp(@RequestBody SignUpRequestDto signUpRequestDto) {
        return authService. signUp(signUpRequestDto);
    }

    @PostMapping("/signin")
    public SignInResponseDto signIn(@RequestBody SignInRequestDto signInRequestDto) {
        return authService.signIn(signInRequestDto);
    }
}
