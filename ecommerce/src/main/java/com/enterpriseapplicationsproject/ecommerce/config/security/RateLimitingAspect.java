package com.enterpriseapplicationsproject.ecommerce.config.security;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Aspect
@Component
@RequiredArgsConstructor
public class RateLimitingAspect {

    private final RateLimitingService rateLimitingService;

    @Around("@annotation(rateLimit) && args(.., request)")
    public Object rateLimit(ProceedingJoinPoint joinPoint, RateLimit rateLimit, HttpServletRequest request) throws Throwable {
        String userId = request.getUserPrincipal().getName(); // Puoi personalizzarlo in base a come gestisci l'ID utente

        if (rateLimitingService.tryAcquire(userId, rateLimit.requests(), 3)) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Too many requests from user " + userId);
        }

        return joinPoint.proceed();
    }
}
