package com.enterpriseapplicationsproject.ecommerce.dto.security;

import lombok.Data;

@Data
public class AuthenticationRequest {
    private String username;
    private String password;

    // Getters and Setters
}
