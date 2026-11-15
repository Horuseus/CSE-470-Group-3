package com.bunon.controller;

import com.bunon.model.User;
import com.bunon.repository.UserRepository;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Runs before EVERY controller and adds the logged-in user to the model under the name "me".
 * That is how the header on every page can show your name and profile picture.
 * "me" is null when nobody is logged in.
 */
@ControllerAdvice
public class GlobalModelAdvice {

    private final UserRepository userRepository;

    public GlobalModelAdvice(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @ModelAttribute("me")
    public User currentUser(Authentication auth) {
        if (auth == null || auth instanceof AnonymousAuthenticationToken) {
            return null;
        }
        return userRepository.findByEmail(auth.getName()).orElse(null);
    }
}
