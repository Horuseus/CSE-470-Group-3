package com.bunon.service;

import com.bunon.model.User;
import com.bunon.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/** Spring Security calls this at login: "find the account for what the person typed (email OR username)". */
@Service
public class AppUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public AppUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String typed) throws UsernameNotFoundException {
        String key = typed.trim().toLowerCase();
        User user = userRepository.findByEmail(key)
                .or(() -> userRepository.findByUsername(key))
                .orElseThrow(() -> new UsernameNotFoundException("No account found"));

        return org.springframework.security.core.userdetails.User   // Spring's own "User" class
                .withUsername(user.getEmail())       // inside the app a person is always identified by email
                .password(user.getPasswordHash())
                .roles(user.getRole().name())
                .build();
    }
}
