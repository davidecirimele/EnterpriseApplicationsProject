package com.enterpriseapplicationsproject.ecommerce.config;

import com.enterpriseapplicationsproject.ecommerce.dto.ServiceError;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class ExceptionHandlerFilter  extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            int status = HttpStatus.UNAUTHORIZED.value();
            String message = "Token not valid";

            ServiceError error = new ServiceError(
                    status,
                    new Date(),
                    request.getRequestURI(),
                    message
            );

            response.setStatus(status);
            response.setContentType("application/json");
            String json = objectMapper.writeValueAsString(error);
            response.getWriter().write(json);
            response.getWriter().flush();
        }
    }


}
