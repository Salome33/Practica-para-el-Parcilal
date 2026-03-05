package com.salo.secure_reactive_api.api.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class DemoController {

    @GetMapping("/me")
    public Mono<Map<String, Object>> me(Authentication auth) {
        return Mono.just(Map.of(
                "email", auth.getName(),
                "roles", auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList()
        ));
    }

    @GetMapping("/admin/secret")
    public Mono<String> adminOnly() {
        return Mono.just("Solo ADMIN puede ver esto");
    }
}