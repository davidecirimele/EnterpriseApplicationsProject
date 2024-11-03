package com.enterpriseapplicationsproject.ecommerce.config.security;

import ch.qos.logback.classic.Logger;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@Aspect
@Component
@RequiredArgsConstructor
public class RateLimitingAspect {

    private final RateLimitingService rateLimitingService;

    @Around("@annotation(rateLimit) && args(.., request)")
    public Object rateLimit(ProceedingJoinPoint joinPoint, RateLimit rateLimit, HttpServletRequest request) throws Throwable {
        // Se HttpServletRequest viene passato come argomento
        return handleRateLimit(joinPoint, rateLimit, request);
    }

    @Around("@annotation(rateLimit)")
    public Object rateLimitNoArgs(ProceedingJoinPoint joinPoint, RateLimit rateLimit) throws Throwable {
        // Se HttpServletRequest non viene passato come argomento, recuperalo dal contesto
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        return handleRateLimit(joinPoint, rateLimit, request);
    }

    private Object handleRateLimit(ProceedingJoinPoint joinPoint, RateLimit rateLimit, HttpServletRequest request) throws Throwable {
        String type = rateLimit.type();
        System.out.println("type: " + type);

        if ("IP".equals(type)) {
            String clientIP = request.getRemoteAddr();
            System.out.println("clientIP: " + clientIP);

            if (!rateLimitingService.tryAcquire(clientIP, rateLimit.requests(), rateLimit.timeWindow())) {
                System.out.println("Rate limit exceeded for IP: " + clientIP);
                throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Too many requests from IP " + clientIP);
            }

        } else if ("USER".equals(type)) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String user = (authentication != null) ? authentication.getName() : null;
            System.out.println("user: " + user);


            if (user == null || !rateLimitingService.tryAcquire(user, rateLimit.requests(), rateLimit.timeWindow())) {
                System.out.println("Rate limit exceeded for User: " + user);
                throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Too many requests from User " + user);
            }
        }

        return joinPoint.proceed();
    }
}
