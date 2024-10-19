package com.enterpriseapplicationsproject.ecommerce.config.security;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)  // L'annotazione è applicabile ai metodi
@Retention(RetentionPolicy.RUNTIME)  // L'annotazione sarà disponibile a runtime
public @interface RateLimit {

    int requests() default 6;  // Numero massimo di richieste consentite
    int timeWindow() default 8;  // Finestra di tempo in secondi
    RateLimitType type() default RateLimitType.IP;  // Tipo di rate limit (per IP o per utente)

}

