package com.enterpriseapplicationsproject.ecommerce.controller;

import com.enterpriseapplicationsproject.ecommerce.data.entities.User;
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

    private final UserService userService;

    private final RefreshTokenService refreshTokenService;

    @GetMapping("/all-users")
    @PreAuthorize("#userId == authentication.principal.getId() or hasRole('USER')")
    public ResponseEntity<List<UserDto>> allUsers() {
        List<UserDto> users = userService.getAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/all-tokens")
    @PreAuthorize("#userId == authentication.principal.getId() or hasRole('USER')")
    public ResponseEntity<List<RefreshTokenDto>> allTokens() {
        List<RefreshTokenDto> tokens = refreshTokenService.getAll();
        return new ResponseEntity<>(tokens, HttpStatus.OK);
    }

}
