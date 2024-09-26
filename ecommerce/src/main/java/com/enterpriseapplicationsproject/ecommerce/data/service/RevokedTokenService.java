package com.enterpriseapplicationsproject.ecommerce.data.service;

public interface RevokedTokenService {
     void revokeToken(String token);
     boolean isTokenRevoked(String token);
}
