package com.enterpriseapplicationsproject.ecommerce.data.service;

import com.enterpriseapplicationsproject.ecommerce.dto.LoginDto;
import com.enterpriseapplicationsproject.ecommerce.dto.SaveUserDto;
import com.enterpriseapplicationsproject.ecommerce.dto.UserLoginDto;

public interface AuthService {
    SaveUserDto registerUser(SaveUserDto userDto);

    String loginUser(UserLoginDto loginDto);
}
