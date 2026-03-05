package com.salo.secure_reactive_api.config;

import com.salo.secure_reactive_api.jwt.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class JwtConfig {

    @Bean
    public JwtService jwtService(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.expMinutes}") long expMinutes
    ) {
        return new JwtService(secret, expMinutes);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}