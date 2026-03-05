package com.salo.secure_reactive_api.auth.service;

import com.salo.secure_reactive_api.jwt.JwtService;
import com.salo.secure_reactive_api.user.model.AppUser;
import com.salo.secure_reactive_api.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class AuthService {

    private final UserRepository repo;
    private final PasswordEncoder encoder;
    private final JwtService jwt;

    public AuthService(UserRepository repo, PasswordEncoder encoder, JwtService jwt) {
        this.repo = repo;
        this.encoder = encoder;
        this.jwt = jwt;
    }

    public Mono<Void> register(String email, String rawPassword) {
        return repo.existsByEmail(email)
                .flatMap(exists -> {
                    if (exists) return Mono.error(new IllegalArgumentException("Email ya registrado"));

                    AppUser u = new AppUser();
                    u.setEmail(email);
                    u.setPasswordHash(encoder.encode(rawPassword));
                    u.setRoles("ROLE_USER");

                    return repo.save(u).then();
                });
    }

    public Mono<String> login(String email, String rawPassword) {
        return repo.findByEmail(email)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Credenciales inválidas")))
                .flatMap(u -> {
                    if (!encoder.matches(rawPassword, u.getPasswordHash())) {
                        return Mono.error(new IllegalArgumentException("Credenciales inválidas"));
                    }
                    List<String> roles = List.of(u.getRoles().split(","));
                    return Mono.just(jwt.generate(u.getEmail(), roles));
                });
    }
}