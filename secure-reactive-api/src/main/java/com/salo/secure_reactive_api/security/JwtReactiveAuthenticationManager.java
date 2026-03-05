package com.salo.secure_reactive_api.security;

import com.salo.secure_reactive_api.jwt.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

public class JwtReactiveAuthenticationManager implements ReactiveAuthenticationManager {

    private final JwtService jwt;

    public JwtReactiveAuthenticationManager(JwtService jwt) {
        this.jwt = jwt;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String token = (String) authentication.getCredentials();

        try {
            Claims claims = jwt.parse(token).getBody();
            String email = claims.getSubject();

            @SuppressWarnings("unchecked")
            List<String> roles = claims.get("roles", List.class);

            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            if (roles != null) {
                for (String r : roles) authorities.add(new SimpleGrantedAuthority(r));
            }

            return Mono.just(new UsernamePasswordAuthenticationToken(email, token, authorities));
        } catch (JwtException e) {
            return Mono.error(new BadCredentialsException("JWT inválido", e));
        }
    }
}