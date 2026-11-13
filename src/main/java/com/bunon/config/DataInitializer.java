package com.bunon.config;

import com.bunon.model.Role;
import com.bunon.model.User;
import com.bunon.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/** Creates a default admin on first start so the team can log in immediately. */
@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner seedAdmin(UserRepository users, PasswordEncoder encoder,
                                @Value("${app.seed-admin.email}") String email,
                                @Value("${app.seed-admin.password}") String password) {
        return args -> {
            if (!users.existsByEmail(email)) {
                User admin = new User();
                admin.setFullName("Bunon Admin");
                admin.setEmail(email);
                admin.setPassword(encoder.encode(password));
                admin.setRole(Role.ADMIN);
                users.save(admin);
            }
        };
    }
}
