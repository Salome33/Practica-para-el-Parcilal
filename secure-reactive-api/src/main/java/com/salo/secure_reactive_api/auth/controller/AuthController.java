package com.salo.secure_reactive_api.auth.controller;

import com.salo.secure_reactive_api.auth.dto.LoginRequest;
import com.salo.secure_reactive_api.auth.dto.RegisterRequest;
import com.salo.secure_reactive_api.auth.dto.TokenResponse;
import com.salo.secure_reactive_api.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService auth;

    public AuthController(AuthService auth) {
        this.auth = auth;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Void> register(@Valid @RequestBody RegisterRequest req) {
        return auth.register(req.getEmail(), req.getPassword());
    }

    @PostMapping("/login")
    public Mono<TokenResponse> login(@Valid @RequestBody LoginRequest req) {
        return auth.login(req.getEmail(), req.getPassword())
                .map(TokenResponse::new);
    }
}