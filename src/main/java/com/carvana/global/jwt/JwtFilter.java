package com.carvana.global.jwt;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

import java.io.IOException;

public class JwtFilter extends GenericFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    private final JwtTokenProvider tokenProvider;

    public JwtFilter(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    // 필터 처리 메서드
    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        try {
            String jwt = resolveToken(httpServletRequest);

            if (StringUtils.hasText(jwt) && this.tokenProvider.validateToken(jwt)) {
                Authentication authentication = this.tokenProvider.getAuthentication(jwt);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else if (StringUtils.hasText(jwt) && !this.tokenProvider.validateToken(jwt)) {
                // 토큰이 있지만 유효하지 않은 경우
                sendErrorResponse(httpServletResponse, "유효하지 않은 토큰입니다.", HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
            filterChain.doFilter(servletRequest, servletResponse);

        } catch (Exception e) {
            sendErrorResponse(httpServletResponse, "인증 처리 중 오류가 발생했습니다: " + e.getMessage(),
                HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    private void sendErrorResponse(HttpServletResponse response, String message, int status) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(status);
        response.getWriter().write("{\"message\": \"" + message + "\", " +
            "\"status\": " + status + ", " +
            "\"error\": \"" + (status == 401 ? "Unauthorized" : "Error") + "\"}");
    }
    // Authorization 헤더에서 토큰 추출 메서드
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);    // Bearer 제거
        }
        return null;
    }
}
