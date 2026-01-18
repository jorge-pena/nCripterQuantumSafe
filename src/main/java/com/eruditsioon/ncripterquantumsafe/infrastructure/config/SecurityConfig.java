package com.eruditsioon.ncripterquantumsafe.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF for API endpoints
                .csrf(AbstractHttpConfigurer::disable)
                // Allow unauthenticated access to specific endpoints
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/qs-crypto/**").permitAll()
                        .anyRequest().authenticated());
        return http.build();
    }
}
