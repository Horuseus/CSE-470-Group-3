package com.bunon.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * The rulebook: who may open which address. Rules are checked from top to bottom, first match wins.
 * Every form we post is protected against forged requests (CSRF) automatically, because Thymeleaf's
 * th:action adds a hidden token to each form.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();      // scrambles passwords so they are never stored as plain text
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(rules -> rules
                // 1. Anyone, even without logging in: shop, passports (customers scan QR codes), login, signup
                .requestMatchers("/", "/shop", "/error", "/css/**", "/uploads/**", "/login", "/signup").permitAll()
                .requestMatchers("/passport", "/passport/*", "/passport/*/qr.png").permitAll()
                // 2. Weaver tools: only weavers and cooperative members
                .requestMatchers("/passport/*/entries", "/looms/**", "/jobs/**", "/designs/**",
                                 "/price/**", "/availability/**").hasAnyRole("WEAVER", "COOPERATIVE_MEMBER")
                // 3. Administrator pages
                .requestMatchers("/admin/**").hasRole("ADMIN")
                // 4. Everything else (like /dashboard): any logged-in user
                .anyRequest().authenticated())
            .formLogin(form -> form
                .loginPage("/login")                 // our own login page instead of Spring's plain one
                .defaultSuccessUrl("/dashboard")
                .permitAll())
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll());
        return http.build();
    }
}
