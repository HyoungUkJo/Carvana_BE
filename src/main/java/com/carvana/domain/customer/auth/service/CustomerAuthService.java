package com.carvana.domain.customer.auth.service;

import com.carvana.domain.customer.auth.dto.*;
import com.carvana.domain.customer.auth.entity.CustomerAuth;
import com.carvana.domain.customer.auth.repository.CustomerAuthRepository;
import com.carvana.domain.customer.member.entity.CustomerMember;
import com.carvana.domain.customer.member.repository.CustomerMemberRepository;
import com.carvana.global.exception.custom.DuplicateEmailException;
import com.carvana.global.exception.custom.IncorrectEmailPasswordException;
import com.carvana.global.jwt.JwtTokenProvider;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Transactional(readOnly = true)  // 원자성 보호를 위해
@RequiredArgsConstructor
public class CustomerAuthService {

    private final CustomerAuthRepository customerAuthRepository;        // AuthRepository 의존관계 설정
    private final CustomerMemberRepository customerMemberRepository;    // MemberRepository 의존관계 설정
    private final PasswordEncoder passwordEncoder;                      // 패스워드 암호화를 위한 인코더 추가
    private final JwtTokenProvider jwtTokenProvider;                    // jwt토큰을 만들기 위한 브로바이더 추가

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
//            .password(passwordEncoder.encode(signUpRequest.getPassword()))    // 패스워드 암호화 -> 개발단계에서는 생략할지 생각필요.
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

        // 이런식으로 패스워드 암호화 하는지 안하는지 service에서 직접 바꾸는건 객체 지향적이지 않은것 같다고 생각. 패스워드 검증 정책 클래스를 따로 만들어서 거기서 처리 필요

        // 패스워드 검사
        if(!Objects.equals(customerAuth.getPassword(), signInRequest.getPassword())){
            throw new IncorrectEmailPasswordException("아이디 또는 비밀번호가 틀립니다.");
        }

        // Todo: 암호화 된 패스워드와 검사
        /*if (!passwordEncoder.matches(signInRequest.getPassword(), customerAuth.getPassword())) {
            throw new IncorrectEmailPasswordException("아이디 또는 비밀번호가 틀립니다.");
        }*/

        CustomerMember customerMember = customerAuth.getCustomerMember();

        // jwt 토큰 생성
        String accessToken = jwtTokenProvider.createAccessToken(customerAuth.getEmail(),"ROLE_USER");


        // 토큰을 포함한 응답
        /*return SignInResponseDto.builder()
            .name(customerMember.getName())
            .accessToken(accessToken)
            .build();*/
        // 토큰 x
        return SignInResponseDto.builder().name(customerMember.getName()).build();
    }

    public EmailCheckResponseDto checkEmailDuplicate(String email) {
        boolean exists = customerAuthRepository.existsByEmail(email);

        return new EmailCheckResponseDto(exists);
    }

    public CustomerMemberDto getCurrentUserInfo() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        CustomerAuth auth = customerAuthRepository.findByEmail(email)
            .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        CustomerMember customerMember = auth.getCustomerMember();

        return new CustomerMemberDto(
            customerMember.getName(),
            customerMember.getPhone(),
            customerMember.getCarType(),
            customerMember.getCarNumber()
        );
    }
}
