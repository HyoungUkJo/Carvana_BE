package com.carvana.domain.owner.auth.service;

import com.carvana.domain.owner.auth.dto.SignInRequestDto;
import com.carvana.domain.owner.auth.dto.SignInResponseDto;
import com.carvana.domain.owner.auth.dto.SignUpResponseDto;
import com.carvana.domain.owner.auth.dto.SignUpRequestDto;
import com.carvana.domain.owner.auth.entity.OwnerAuth;
import com.carvana.domain.owner.auth.repository.OwnerAuthRepository;
import com.carvana.domain.owner.member.entity.OwnerMember;
import com.carvana.domain.owner.member.repository.OwnerMemberRepository;
import com.carvana.global.exception.custom.DuplicateEmailException;
import com.carvana.global.exception.custom.IncorrectEmailPasswordException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Transactional(readOnly = true)  // 원자성 보호를 위해
@RequiredArgsConstructor
public class OwnerAuthService {

    private final OwnerAuthRepository ownerAuthRepository;        // AuthRepository 의존관계 설정
    private final OwnerMemberRepository ownerMemberRepository;    // MemberRepository 의존관계 설정

    @Transactional
    public SignUpResponseDto signUp(SignUpRequestDto signUpRequest) {
        // 이메일 중복검사
        if (ownerAuthRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new DuplicateEmailException("이미 존재하는 이메일입니다.");
        }
        // 패스워드 유효성 검사 -> 이후에 설정
        // validatePassword()

        // 멤버 엔티티 생성
        OwnerMember ownerMember = new OwnerMember(signUpRequest.getName(), signUpRequest.getPhone());
        ownerMemberRepository.save(ownerMember);

        // auth 엔티티 생성 저장 -> 인증 토큰 관리 -> 추후 레디스로
        // Todo: 패스워드 암호화
        OwnerAuth ownerAuth = OwnerAuth.builder()
            .email(signUpRequest.getEmail())
            .password(signUpRequest.getPassword())
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
        // 성공여부 리턴 추후 Jwt 리턴
        return SignInResponseDto.builder().name(ownerMember.getName()).build();
    }
}
