package com.carvana.domain.user.auth.service;

import com.carvana.domain.user.auth.dto.SignInRequestDto;
import com.carvana.domain.user.auth.dto.SignInResponseDto;
import com.carvana.domain.user.auth.dto.SignUpResponseDto;
import com.carvana.domain.user.auth.dto.SignUpRequestDto;
import com.carvana.domain.user.auth.entity.Auth;
import com.carvana.domain.user.auth.repository.AuthRepository;
import com.carvana.domain.user.member.entity.Member;
import com.carvana.domain.user.member.repository.MemberRepository;
import com.carvana.global.exception.custom.DuplicateEmailException;
import com.carvana.global.exception.custom.IncorrectEmailPasswordException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Transactional(readOnly = true)  // 원자성 보호를 위해
@RequiredArgsConstructor
public class AuthService {

    private final AuthRepository authRepository;        // AuthRepository 의존관계 설정
    private final MemberRepository memberRepository;    // MemberRepository 의존관계 설정

    @Transactional
    public SignUpResponseDto signUp(SignUpRequestDto signUpRequest) {
        // 이메일 중복검사
        if (authRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new DuplicateEmailException("이미 존재하는 이메일입니다.");
        }
        // 패스워드 유효성 검사 -> 이후에 설정
        // validatePassword()

        // 멤버 엔티티 생성
        Member member = new Member(signUpRequest.getName(), signUpRequest.getPhone());
        memberRepository.save(member);

        // auth 엔티티 생성 저장 -> 인증 토큰 관리 -> 추후 레디스로
        // Todo: 패스워드 암호화
        Auth auth = Auth.builder()
            .email(signUpRequest.getEmail())
            .password(signUpRequest.getPassword())
            .member(member)
            .build();
        authRepository.save(auth);

        // 회원가입 결과 return
        return SignUpResponseDto.builder()
            .email(auth.getEmail())
            .name(member.getName())
            .build();
    }

    public SignInResponseDto signIn(SignInRequestDto signInRequest) {
        // 이메일 검사
        Auth auth = authRepository.findByEmail(signInRequest.getEmail())
            .orElseThrow(() -> new IncorrectEmailPasswordException("아이디 또는 비밀번호가 틀립니다."));

        // 패스워드 검사
        // Todo: 암호화 된 패스워드와 검사
        if(!Objects.equals(auth.getPassword(), signInRequest.getPassword())){
            throw new IncorrectEmailPasswordException("아이디 또는 비밀번호가 틀립니다.");
        }

        Member member = auth.getMember();
        // 성공여부 리턴 추후 Jwt 리턴
        return SignInResponseDto.builder().name(member.getName()).build();
    }
}
