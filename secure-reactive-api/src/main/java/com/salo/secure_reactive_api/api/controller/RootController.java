package com.salo.secure_reactive_api.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
public class RootController {

    @GetMapping("/")
    public Mono<Map<String, Object>> root() {
        return Mono.just(Map.of(
                "status", "ok",
                "service", "secure-reactive-api"
        ));
    }
}