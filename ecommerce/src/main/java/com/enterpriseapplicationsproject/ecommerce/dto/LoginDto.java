package com.enterpriseapplicationsproject.ecommerce.dto;

import lombok.Data;

@Data
public class LoginDto {
    private String accessToken;
    private String refreshToken;
    private Long id;
    private String username;
    private String email;
    private String role;

}
