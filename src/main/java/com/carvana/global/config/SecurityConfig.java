package com.carvana.global.config;

import com.carvana.global.jwt.JwtFilter;
import com.carvana.global.jwt.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtTokenProvider tokenProvider;

    public SecurityConfig(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    // 패스워드 인코더 빈 등록
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 보안 필터 체인 설정
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())   // CSRF 보호 비활성화 JWT는 안전함
            .sessionManagement(sesion -> sesion     // 세션 생성 x -> jwt는 stateless
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(auth -> auth     // 요청 URL 접근 권한 설정 로그인, 회원가입, 계정찾기? 등 인증이 필요없는 api는 따로 등록해야함
                .requestMatchers("/api/user/auth/signin").permitAll()
                .requestMatchers("/api/user/auth/signup").permitAll()
                .requestMatchers("/api/user/auth/email-exists").permitAll()
                // Swagger UI 관련 경로 추가
                .requestMatchers("/swagger-ui/**").permitAll()
                .requestMatchers("/swagger-resources/**").permitAll()
                .requestMatchers("/v3/api-docs/**").permitAll()
                .requestMatchers("/swagger-ui.html").permitAll()
                .anyRequest().authenticated()
            ).addFilterBefore(new JwtFilter(tokenProvider),     // JWT 필터 추가
                UsernamePasswordAuthenticationFilter.class);

        return http.build();

    }


}
