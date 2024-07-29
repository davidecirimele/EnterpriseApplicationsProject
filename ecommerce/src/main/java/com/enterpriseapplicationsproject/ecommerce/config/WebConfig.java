package com.enterpriseapplicationsproject.ecommerce.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:8080") // permette solo le richieste dall'origine specificata
                .allowedMethods("GET", "POST", "PUT", "DELETE") // permette solo determinati metodi HTTP
                .allowedHeaders("Authorization", "Content-Type") // permette solo determinati header
                .allowCredentials(false) // abilita l'invio dei cookie di autenticazione
                .maxAge(3600); // massimo tempo di caching delle configurazioni CORS in secondi
    }
}
