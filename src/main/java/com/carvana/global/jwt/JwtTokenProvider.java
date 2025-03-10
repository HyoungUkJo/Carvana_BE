package com.carvana.global.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

@PropertySource("classpath:config/secrets.properties")
@Component
public class JwtTokenProvider {

    private final SecretKey key;
    private final long accessTokenSesson;

    public JwtTokenProvider(@Value("${JWT_SECRET_KEY}") String secret, @Value("${JWT_VALIDITY_IN_SECONDS}") long accessTokenSesson) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessTokenSesson = accessTokenSesson * 1000;
    }

    // JWT 액세스 토큰 생성
    public String createAccessToken(String email, Collection<String> authorities) {
        long now = (new Date()).getTime();
        Date validity = new Date(now + this.accessTokenSesson);

        String authoritiesString = String.join(",", authorities);

        return Jwts.builder()
            .subject(email)
            .claim("auth", authoritiesString)
            .issuedAt(new Date())
            .expiration(validity)
            .signWith(key)
            .compact();
    }

    // 인증 정보 추출
    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .getPayload();

        Collection<? extends GrantedAuthority> authorities =
            Arrays.stream(claims.get("auth").toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .toList();

        // 인증된 사용자정보 생성
        User principal = new User(claims.getSubject(), "", authorities);

        // 인증 객체 생성 및 반환
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    // 토큰 유효성 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException exception) {
            return false;
        }
    }

    // 토큰에서 사용자 이메일 추출 (subject 추출)
    public String getEmailFromToken(String token) {
        return Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getSubject();
    }

    // 토큰 만료시간 확인
    public boolean isTokenExpired(String token) {
        try {
            Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

            return claims.getExpiration().before(new Date());
        } catch (JwtException e) {
            return true;
        }
    }

}
