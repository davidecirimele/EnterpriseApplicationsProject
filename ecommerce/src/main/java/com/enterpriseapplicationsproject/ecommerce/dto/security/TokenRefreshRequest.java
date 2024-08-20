package com.enterpriseapplicationsproject.ecommerce.dto.security;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TokenRefreshRequest {

    @NotBlank(message = "Token is mandatory")
    @NotNull
    private String refreshToken;

}
