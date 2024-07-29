package com.enterpriseapplicationsproject.ecommerce.controller;


import com.enterpriseapplicationsproject.ecommerce.config.security.JwtService;
import com.enterpriseapplicationsproject.ecommerce.config.security.LoggedUserDetailsService;
import com.enterpriseapplicationsproject.ecommerce.data.service.AuthService;
import com.enterpriseapplicationsproject.ecommerce.data.service.UserService;
import com.enterpriseapplicationsproject.ecommerce.dto.SaveUserDto;
import com.enterpriseapplicationsproject.ecommerce.dto.UserDto;
import com.enterpriseapplicationsproject.ecommerce.dto.UserLoginDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final LoggedUserDetailsService loggedUserDetailsService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final AuthService authService;

    @PostMapping(consumes = "application/json", path = "/register")
    public ResponseEntity<SaveUserDto> registerUser(@RequestBody  SaveUserDto userDto) {
        return ResponseEntity.ok(authService.registerUser(userDto));
    }

    @PostMapping(consumes = "application/json", path = "/login")
    public ResponseEntity<String> login(@RequestBody UserLoginDto userDto) {
        return ResponseEntity.ok(authService.loginUser(userDto));
    }

}
