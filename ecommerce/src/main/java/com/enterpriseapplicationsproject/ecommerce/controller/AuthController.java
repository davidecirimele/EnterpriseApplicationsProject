package com.enterpriseapplicationsproject.ecommerce.controller;


import com.enterpriseapplicationsproject.ecommerce.config.security.JwtService;
import com.enterpriseapplicationsproject.ecommerce.config.security.LoggedUserDetails;
import com.enterpriseapplicationsproject.ecommerce.config.security.LoggedUserDetailsService;
import com.enterpriseapplicationsproject.ecommerce.config.security.RateLimit;
import com.enterpriseapplicationsproject.ecommerce.data.service.AuthService;
import com.enterpriseapplicationsproject.ecommerce.data.service.RefreshTokenService;
import com.enterpriseapplicationsproject.ecommerce.data.service.UserService;
import com.enterpriseapplicationsproject.ecommerce.dto.*;
import com.enterpriseapplicationsproject.ecommerce.dto.security.AccessTokenValidationDto;
import com.enterpriseapplicationsproject.ecommerce.dto.security.AuthenticationResponse;
import com.enterpriseapplicationsproject.ecommerce.dto.security.RefreshTokenDto;
import com.enterpriseapplicationsproject.ecommerce.dto.security.TokenRefreshRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final AuthService authService;

    private final RefreshTokenService refreshTokenService;


    private final LoggedUserDetailsService loggedUserDetailsService;


    @RateLimit(type = "USER")
    @PostMapping(consumes = "application/json", path = "/register")
    public ResponseEntity<UserDetailsDto> registerUser(@Valid @RequestBody SaveUserDto userDto) {
        return ResponseEntity.ok(authService.registerUser(userDto));
    }

    @RateLimit(type = "USER")
    @PostMapping(consumes = "application/json", path = "/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody CredentialDto credentials) {
        log.info("Received request for auth/login");
        return ResponseEntity.ok(authService.loginUser(credentials));
    }

    /*@GetMapping("/refreshToken")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                Map<String, String> tokenMap = authService.refreshToken(authorizationHeader, request.getRequestURL().toString());
                response.addHeader("access_token", tokenMap.get("access_token"));
                response.addHeader("refresh_token", tokenMap.get("refresh_token"));
            }
            catch (Exception e) {
                //log.error(String.format("Error refresh token: %s", authorizationHeader), e);
                response.setStatus(FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("errorMessage", e.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        } else {
            throw new RuntimeException("Refresh token is missing");
        }
    }*/
    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
        String refreshToken = request.getRefreshToken();

        // Verifica se il refresh token esiste nel database
        RefreshTokenDto storedToken = refreshTokenService.findByToken(refreshToken);

        if (storedToken == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid refresh token");
        }

        if (jwtService.isTokenExpired(refreshToken)) {
            refreshTokenService.revokeRefreshTokenByToken(refreshToken);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Refresh token is expired");
        }

        String username = jwtService.extractUsername(refreshToken);
        final UserDetails userDetails = loggedUserDetailsService.loadUserByUsername(username);

        final String newAccessToken = jwtService.generateToken(userDetails);
        final String newRefreshToken = jwtService.generateRefreshToken(userDetails, 96);

        refreshTokenService.revokeRefreshTokenByToken(refreshToken);
        refreshTokenService.save(new RefreshTokenDto(newRefreshToken), userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(newAccessToken, newRefreshToken));
    }
    @PostMapping("/validate-token")
    public ResponseEntity<AccessTokenValidationDto> validateToken(@RequestBody AccessTokenValidationDto request) {
        return ResponseEntity.ok(authService.validateToken(request));
    }

}
