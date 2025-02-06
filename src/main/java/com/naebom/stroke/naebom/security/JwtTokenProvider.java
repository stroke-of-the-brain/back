package com.naebom.stroke.naebom.security;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.logging.Logger;

@Component
public class JwtTokenProvider {

    private static final Logger logger = Logger.getLogger(JwtTokenProvider.class.getName());

    private final String secretKey;
    private final long validityInMilliseconds = 86400000; // 24시간

    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
        this.secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8)); // Base64 인코딩
    }

    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + validityInMilliseconds))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            logger.warning("JWT 토큰이 만료되었습니다: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.warning("지원되지 않는 JWT 토큰입니다: " + e.getMessage());
        } catch (MalformedJwtException e) {
            logger.warning("잘못된 형식의 JWT 토큰입니다: " + e.getMessage());
        } catch (SignatureException e) {
            logger.warning("JWT 서명이 올바르지 않습니다: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.warning("JWT 토큰이 비어있거나 잘못되었습니다: " + e.getMessage());
        }
        return false;
    }

    public String getEmailFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
