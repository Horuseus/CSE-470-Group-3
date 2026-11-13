package com.bunon.service;

import com.bunon.model.User;
import com.bunon.model.Weaver;
import com.bunon.repository.UserRepository;
import com.bunon.repository.WeaverRepository;
import org.springframework.stereotype.Service;

import java.security.Principal;

/** Shared helper: turns the logged-in Principal into User / Weaver. Everyone uses this. */
@Service
public class CurrentUserService {

    private final UserRepository userRepository;
    private final WeaverRepository weaverRepository;

    public CurrentUserService(UserRepository userRepository, WeaverRepository weaverRepository) {
        this.userRepository = userRepository;
        this.weaverRepository = weaverRepository;
    }

    public User getUser(Principal principal) {
        return userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new IllegalStateException("Logged-in user not found"));
    }

    public Weaver getWeaver(Principal principal) {
        return weaverRepository.findByUser(getUser(principal))
                .orElseThrow(() -> new IllegalStateException("Logged-in user is not a weaver"));
    }
}
