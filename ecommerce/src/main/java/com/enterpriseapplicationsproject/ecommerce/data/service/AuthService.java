package com.enterpriseapplicationsproject.ecommerce.data.service;

import com.enterpriseapplicationsproject.ecommerce.dto.*;
import com.enterpriseapplicationsproject.ecommerce.dto.security.AccessTokenValidationDto;

import java.util.Map;

public interface AuthService {
    UserDetailsDto registerUser(SaveUserDto userDto);

    UserDetailsDto registerAdmin(SaveUserDto userDto);

    Map<String, String> loginUser(CredentialDto loginDto);


    Map<String, String> refreshToken(String authorizationHeader, String toString);

    AccessTokenValidationDto validateToken(AccessTokenValidationDto accessTokenValidationDto);
}
