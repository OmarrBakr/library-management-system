package com.example.task2.config;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Collection;

@Getter
public class JwtAuthenticationToken extends UsernamePasswordAuthenticationToken {
    private final String jwt;

    public JwtAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities, String jwt) {
        super(principal, credentials, authorities);
        this.jwt = jwt;
    }
}
