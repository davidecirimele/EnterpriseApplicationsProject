package com.enterpriseapplicationsproject.ecommerce.dto.security;

import lombok.Data;

@Data
public class TokenRefreshRequest {

    private String refreshToken;

}
