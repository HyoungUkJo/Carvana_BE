package com.carvana.domain.customer.auth.controller;

import com.carvana.domain.customer.auth.dto.*;
import com.carvana.domain.customer.auth.service.CustomerAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
}
