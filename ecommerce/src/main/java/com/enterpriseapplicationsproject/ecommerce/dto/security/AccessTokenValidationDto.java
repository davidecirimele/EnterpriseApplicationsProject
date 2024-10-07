package com.enterpriseapplicationsproject.ecommerce.dto.security;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AccessTokenValidationDto {
    @NotNull
    private String token;
}
