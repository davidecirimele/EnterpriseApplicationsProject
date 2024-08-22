package com.enterpriseapplicationsproject.ecommerce.controller;

import com.enterpriseapplicationsproject.ecommerce.data.entities.User;
import com.enterpriseapplicationsproject.ecommerce.data.service.AuthService;
import com.enterpriseapplicationsproject.ecommerce.data.service.RefreshTokenService;
import com.enterpriseapplicationsproject.ecommerce.data.service.UserService;
import com.enterpriseapplicationsproject.ecommerce.dto.*;
import com.enterpriseapplicationsproject.ecommerce.dto.security.RefreshTokenDto;
import com.enterpriseapplicationsproject.ecommerce.exception.UserRegistrationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/admin")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class AdminController {

    private final AuthService authService;
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping(consumes = "application/json", path = "/register")
    public ResponseEntity<SaveUserDto> registerUser(@RequestBody  SaveUserDto userDto) {
        return ResponseEntity.ok(authService.registerAdmin(userDto));
    }

    @GetMapping("/all-users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDto>> allUsers() {
        List<UserDto> users = userService.getAllDto();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/all-tokens")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RefreshTokenDto>> allTokens() {
        List<RefreshTokenDto> tokens = refreshTokenService.getAll();
        return new ResponseEntity<>(tokens, HttpStatus.OK);
    }

}
