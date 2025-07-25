package com.carvana.domain.owner.auth.service;

import com.carvana.domain.customer.auth.dto.CustomerMemberDto;
import com.carvana.domain.customer.auth.entity.CustomerAuth;
import com.carvana.domain.customer.member.entity.CustomerMember;
import com.carvana.domain.owner.auth.dto.*;
import com.carvana.domain.owner.auth.entity.OwnerAuth;
import com.carvana.domain.owner.auth.repository.OwnerAuthRepository;
import com.carvana.domain.owner.member.entity.OwnerMember;
import com.carvana.domain.owner.member.repository.OwnerMemberRepository;
import com.carvana.global.exception.custom.DuplicateEmailException;
import com.carvana.global.exception.custom.IncorrectEmailPasswordException;
import com.carvana.global.jwt.JwtTokenProvider;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Transactional(readOnly = true)  // 원자성 보호를 위해
@RequiredArgsConstructor
public class OwnerAuthService {

    private final OwnerAuthRepository ownerAuthRepository;        // AuthRepository 의존관계 설정
    private final OwnerMemberRepository ownerMemberRepository;    // MemberRepository 의존관계 설정
    private final JwtTokenProvider jwtTokenProvider;                // jwtToken Provider 의존관계 설정

    @Transactional
    public SignUpResponseDto signUp(SignUpRequestDto signUpRequestDto) {
        // 이메일 중복검사
        if (ownerAuthRepository.existsByEmail(signUpRequestDto.getEmail())) {
            throw new DuplicateEmailException("이미 존재하는 이메일입니다.");
        }
        // 패스워드 유효성 검사 -> 이후에 설정
        // validatePassword()

        // 멤버 엔티티 생성
        OwnerMember ownerMember = OwnerMember.builder()
            .address(signUpRequestDto.getAddress())
            .phone(signUpRequestDto.getPhone())
            .typeOfBusiness(signUpRequestDto.getTypeOfBusiness())
            .name(signUpRequestDto.getName())
            .build();
        ownerMemberRepository.save(ownerMember);

        // auth 엔티티 생성 저장 -> 인증 토큰 관리 -> 추후 레디스로
        // Todo: 패스워드 암호화
        OwnerAuth ownerAuth = OwnerAuth.builder()
            .email(signUpRequestDto.getEmail())
            .password(signUpRequestDto.getPassword())
            .ownerMember(ownerMember)
            .build();
        ownerAuthRepository.save(ownerAuth);

        // 회원가입 결과 return
        return SignUpResponseDto.builder()
            .email(ownerAuth.getEmail())
            .name(ownerMember.getName())
            .build();
    }

    public SignInResponseDto signIn(SignInRequestDto signInRequest) {
        // 이메일 검사
        OwnerAuth ownerAuth = ownerAuthRepository.findByEmail(signInRequest.getEmail())
            .orElseThrow(() -> new IncorrectEmailPasswordException("아이디 또는 비밀번호가 틀립니다."));

        // 패스워드 검사
        // Todo: 암호화 된 패스워드와 검사
        if(!Objects.equals(ownerAuth.getPassword(), signInRequest.getPassword())){
            throw new IncorrectEmailPasswordException("아이디 또는 비밀번호가 틀립니다.");
        }

        OwnerMember ownerMember = ownerAuth.getOwnerMember();

        String accessToken = jwtTokenProvider.createAccessToken(ownerAuth.getEmail(), "ROLE_OWNER");

        // 성공여부 리턴 추후 Jwt 리턴
        return SignInResponseDto.builder()
            .name(ownerMember.getName())
            .accessToken(accessToken)
            .build();
    }

    // 토큰을 통해
    // 로그인한 계정 알려주는 로직
    public OwnerMemberDto getCurrentUserInfo() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        OwnerAuth auth = ownerAuthRepository.findByEmail(email)
            .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        OwnerMember ownerMember = auth.getOwnerMember();

        return new OwnerMemberDto(
            ownerMember.getName(),
            ownerMember.getPhone(),
            ownerMember.getBusinessNumber(),
            ownerMember.getCarWashes(),
            ownerMember.getAddress()
        );
    }

}
