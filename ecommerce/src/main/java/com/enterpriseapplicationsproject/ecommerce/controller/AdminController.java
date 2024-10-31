package com.enterpriseapplicationsproject.ecommerce.controller;

import com.enterpriseapplicationsproject.ecommerce.config.security.RateLimit;
import com.enterpriseapplicationsproject.ecommerce.data.entities.User;
import com.enterpriseapplicationsproject.ecommerce.data.service.AuthService;
import com.enterpriseapplicationsproject.ecommerce.data.service.OrdersService;
import com.enterpriseapplicationsproject.ecommerce.data.service.RefreshTokenService;
import com.enterpriseapplicationsproject.ecommerce.data.service.UserService;
import com.enterpriseapplicationsproject.ecommerce.dto.*;
import com.enterpriseapplicationsproject.ecommerce.dto.security.RefreshTokenDto;
import com.enterpriseapplicationsproject.ecommerce.exception.UserRegistrationException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("api/v1/admin")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class AdminController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final OrdersService ordersService;


    @RateLimit
    @PostMapping(consumes = "application/json", path = "/register")
    public ResponseEntity<UserDetailsDto> registerAdmin(@Valid @RequestBody  SaveUserDto userDto) {
        return ResponseEntity.ok(authService.registerAdmin(userDto));
    }

    @RateLimit(type ="USER")
    @GetMapping("/all-tokens")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RefreshTokenDto>> allTokens() {
        List<RefreshTokenDto> tokens = refreshTokenService.getAll();
        return new ResponseEntity<>(tokens, HttpStatus.OK);
    }

    @GetMapping("/all-orders")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<OrderSummaryDto>> allOrders( @RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<OrderSummaryDto> orders = ordersService.getAll(pageable);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }



}
