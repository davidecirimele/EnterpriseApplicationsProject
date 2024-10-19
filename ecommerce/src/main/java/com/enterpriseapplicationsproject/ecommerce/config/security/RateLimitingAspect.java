package com.enterpriseapplicationsproject.ecommerce.config.security;

import ch.qos.logback.classic.Logger;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
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
    private static final Logger log = (Logger) org.slf4j.LoggerFactory.getLogger(RateLimitingAspect.class);

    @Around("@annotation(rateLimit)")
    public Object rateLimit(ProceedingJoinPoint joinPoint, RateLimit rateLimit) throws Throwable {

        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();

        System.out.println("RateLimitingAspect.rateLimit");
        String type = rateLimit.type();
        System.out.println("type: " + type);

        if (type.equals("IP")) {
            String clientIP = request.getRemoteAddr();
            System.out.println("Checking rate limit for IP: " + clientIP);

            if (!rateLimitingService.tryAcquireForIp(clientIP)) {
                System.out.println("Rate limit exceeded for IP: " + clientIP);
                throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Too many requests from IP " + clientIP);
            }

        } else if (type.equals("USER")) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String user = (authentication != null) ? authentication.getName() : null;
            System.out.println("Checking rate limit for User: " + user);
            System.out.println("Rate at which user can access: " + rateLimitingService);

            if (user == null || !rateLimitingService.tryAcquireForUser(user)) {
                System.out.println("Rate limit exceeded for User: " + user);
                throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Too many requests from User " + user);
            }
        }

        return joinPoint.proceed();
    }
}
