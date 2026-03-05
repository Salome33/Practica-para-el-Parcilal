package com.salo.secure_reactive_api.user.service;

import com.salo.secure_reactive_api.user.model.AppUser;
import com.salo.secure_reactive_api.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserService {

    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    public Mono<AppUser> findByEmail(String email) {
        return repo.findByEmail(email);
    }
}