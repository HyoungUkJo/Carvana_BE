package com.carvana.domain.customer.auth.service;

import com.carvana.domain.customer.auth.dto.*;
import com.carvana.domain.customer.auth.entity.CustomerAuth;
import com.carvana.domain.customer.auth.repository.CustomerAuthRepository;
import com.carvana.domain.customer.member.entity.CustomerMember;
import com.carvana.domain.customer.member.repository.CustomerMemberRepository;
import com.carvana.global.exception.custom.DuplicateEmailException;
import com.carvana.global.exception.custom.IncorrectEmailPasswordException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Transactional(readOnly = true)  // 원자성 보호를 위해
@RequiredArgsConstructor
public class CustomerAuthService {

    private final CustomerAuthRepository customerAuthRepository;        // AuthRepository 의존관계 설정
    private final CustomerMemberRepository customerMemberRepository;    // MemberRepository 의존관계 설정

    @Transactional
    public SignUpResponseDto signUp(SignUpRequestDto signUpRequest) {
        // 이메일 중복검사
        if (customerAuthRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new DuplicateEmailException("이미 존재하는 이메일입니다.");
        }
        // 패스워드 유효성 검사 -> 이후에 설정
        // validatePassword()

        // 멤버 엔티티 생성
        CustomerMember customerMember = new CustomerMember(signUpRequest.getName(), signUpRequest.getPhone(),signUpRequest.getCarType(), signUpRequest.getCarNumber());
        customerMemberRepository.save(customerMember);

        // auth 엔티티 생성 저장 -> 인증 토큰 관리 -> 추후 레디스로
        // Todo: 패스워드 암호화
        CustomerAuth customerAuth = CustomerAuth.builder()
            .email(signUpRequest.getEmail())
            .password(signUpRequest.getPassword())
            .customerMember(customerMember)
            .build();
        customerAuthRepository.save(customerAuth);

        // 회원가입 결과 return
        return SignUpResponseDto.builder()
            .email(customerAuth.getEmail())
            .name(customerMember.getName())
            .build();
    }

    public SignInResponseDto signIn(SignInRequestDto signInRequest) {
        // 이메일 검사
        CustomerAuth customerAuth = customerAuthRepository.findByEmail(signInRequest.getEmail())
            .orElseThrow(() -> new IncorrectEmailPasswordException("아이디 또는 비밀번호가 틀립니다."));

        // 패스워드 검사
        // Todo: 암호화 된 패스워드와 검사
        if(!Objects.equals(customerAuth.getPassword(), signInRequest.getPassword())){
            throw new IncorrectEmailPasswordException("아이디 또는 비밀번호가 틀립니다.");
        }

        CustomerMember customerMember = customerAuth.getCustomerMember();
        // 성공여부 리턴 추후 Jwt 리턴
        return SignInResponseDto.builder().name(customerMember.getName()).build();
    }

    public EmailCheckResponseDto checkEmailDuplicate(String email) {
        boolean exists = customerAuthRepository.existsByEmail(email);

        return new EmailCheckResponseDto(exists);
    }
}
