package com.enterpriseapplicationsproject.ecommerce.config.security;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@Aspect
@Component
@RequiredArgsConstructor
public class RateLimitingAspect {

    private final RateLimitingService rateLimitingService;

    @Around("@annotation(rateLimit) && args(.., request)")
    public Object rateLimit(ProceedingJoinPoint joinPoint, RateLimit rateLimit, HttpServletRequest request) throws Throwable {

        String type = rateLimit.type().toString();
        if (type.equals("IP")) {
            String clientIP = request.getRemoteAddr();

            if (rateLimitingService.tryAcquireForIp(clientIP)) {
                throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Too many requests from IP " + clientIP);
            }

        } else if (type.equals("USER")) {
            String user = request.getRemoteUser();

            if (rateLimitingService.tryAcquireForUser(user)) {
                throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Too many requests from IP " + user);
            }
        }
        return joinPoint.proceed();
    }
}
