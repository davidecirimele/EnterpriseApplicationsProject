package com.enterpriseapplicationsproject.ecommerce.data.service;

import com.enterpriseapplicationsproject.ecommerce.dto.LoginDto;
import com.enterpriseapplicationsproject.ecommerce.dto.SaveUserDto;
import com.enterpriseapplicationsproject.ecommerce.dto.UserLoginDto;

import java.util.Map;

public interface AuthService {
    SaveUserDto registerUser(SaveUserDto userDto);

    SaveUserDto registerAdmin(SaveUserDto userDto);

    Map<String, String> loginUser(UserLoginDto loginDto);

    Map<String, String> refreshToken(String authorizationHeader, String toString);
}
