package com.salo.secure_reactive_api.user.repository;

import com.salo.secure_reactive_api.user.model.AppUser;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<AppUser, Long> {
    Mono<AppUser> findByEmail(String email);
    Mono<Boolean> existsByEmail(String email);
}