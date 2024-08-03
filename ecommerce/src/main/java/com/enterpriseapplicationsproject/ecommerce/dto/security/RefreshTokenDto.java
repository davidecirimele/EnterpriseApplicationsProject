package com.enterpriseapplicationsproject.ecommerce.dto.security;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RefreshTokenDto {
    private String token;

    public RefreshTokenDto() {}
    public RefreshTokenDto(String token){
        this.token = token;
    }

}
