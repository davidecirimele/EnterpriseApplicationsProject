package com.enterpriseapplicationsproject.ecommerce;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class TestSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // Disabilita CSRF
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()  // Permetti tutte le richieste senza autenticazione
                );
        return http.build();
    }
}


