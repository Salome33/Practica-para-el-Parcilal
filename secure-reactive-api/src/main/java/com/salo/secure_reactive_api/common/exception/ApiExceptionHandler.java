package com.salo.secure_reactive_api.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<Map<String, Object>> badRequest(IllegalArgumentException ex) {
        return Mono.just(Map.of(
                "error", "BAD_REQUEST",
                "message", ex.getMessage()
        ));
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Mono<Map<String, Object>> unauthorized(BadCredentialsException ex) {
        return Mono.just(Map.of(
                "error", "UNAUTHORIZED",
                "message", "Token inválido o expirado"
        ));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Mono<Map<String, Object>> serverError(Exception ex) {
        return Mono.just(Map.of(
                "error", "INTERNAL_SERVER_ERROR",
                "message", "Ocurrió un error inesperado"
        ));
    }
}