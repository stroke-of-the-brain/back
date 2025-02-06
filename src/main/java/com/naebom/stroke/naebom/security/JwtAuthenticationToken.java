package com.naebom.stroke.naebom.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final Object principal;
    private Object credentials;

    public JwtAuthenticationToken(Object principal, Object credentials) {
        super(Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))); // 기본 권한 부여
        this.principal = principal;
        this.credentials = credentials;
        setAuthenticated(true); // 인증된 상태로 설정
    }

    // 권한이 있는 경우 맞게 설정
    public JwtAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(authorities != null ? authorities : Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        this.principal = principal;
        this.credentials = credentials;
        setAuthenticated(true); // 인증된 상태로 설정
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        credentials = null;
    }
}
