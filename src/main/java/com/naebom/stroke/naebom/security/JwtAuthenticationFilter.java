package com.naebom.stroke.naebom.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // 토큰 추출
        String token = resolveToken(request);
        if (token != null) {
            System.out.println("Extracted Token: " + token);
        }

        // 토큰 검증 및 SecurityContext 설정
        if (token != null && jwtTokenProvider.validateToken(token)) {
            String email = jwtTokenProvider.getEmailFromToken(token);
            System.out.println("Valid Token for Email: " + email);
            SecurityContextHolder.getContext().setAuthentication(
                    new JwtAuthenticationToken(email, null, null)
            );
        } else {
            if (token == null) {
                System.out.println("No token found in the request.");
            } else {
                System.out.println("Invalid Token.");
            }
        }

        // 다음 필터 호출
        filterChain.doFilter(request, response);
    }

    /**
     * HTTP 요청 헤더에서 Authorization 헤더를 읽어 Bearer 토큰을 추출합니다.
     * @param request HTTP 요청 객체
     * @return Bearer 토큰 문자열 또는 null
     */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // Bearer를 제거하고 반환
        }
        return null; // Bearer 토큰이 없는 경우 null 반환
    }
}
