package com.enterpriseapplicationsproject.ecommerce.data.service.impl;

import com.enterpriseapplicationsproject.ecommerce.config.security.JwtService;
import com.enterpriseapplicationsproject.ecommerce.config.security.LoggedUserDetails;
import com.enterpriseapplicationsproject.ecommerce.config.security.RateLimit;
import com.enterpriseapplicationsproject.ecommerce.data.dao.ShoppingCartsDao;
import com.enterpriseapplicationsproject.ecommerce.data.dao.UsersDao;
import com.enterpriseapplicationsproject.ecommerce.data.entities.Admin;
import com.enterpriseapplicationsproject.ecommerce.data.entities.RefreshToken;
import com.enterpriseapplicationsproject.ecommerce.data.entities.ShoppingCart;
import com.enterpriseapplicationsproject.ecommerce.data.entities.User;
import com.enterpriseapplicationsproject.ecommerce.data.service.RefreshTokenService;
import com.enterpriseapplicationsproject.ecommerce.data.service.RevokedTokenService;
import com.enterpriseapplicationsproject.ecommerce.dto.*;
import com.enterpriseapplicationsproject.ecommerce.dto.security.AccessTokenValidationDto;
import com.enterpriseapplicationsproject.ecommerce.dto.security.RefreshTokenDto;
import com.enterpriseapplicationsproject.ecommerce.exception.LoginException;
import com.enterpriseapplicationsproject.ecommerce.exception.UserAlreadyExistsException;
import com.enterpriseapplicationsproject.ecommerce.exception.UserNotFoundException;
import io.jsonwebtoken.JwtException;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;

import com.enterpriseapplicationsproject.ecommerce.data.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements  AuthService{

    private final UsersDao userDao;
    private final ShoppingCartsDao shoppingCartsDao;
    private final ModelMapper modelMapper;
    private final JwtService jwtService;

    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    private final RevokedTokenService revokedTokenService;

    private final Integer expirationTimeInHours = 96;


    @RateLimit(type = "USER")
    @Override
    public UserDetailsDto registerUser(@Valid SaveUserDto userDto) {
        System.out.println("UserDto: " + userDto);

        userDao.findByCredentialEmail(userDto.getCredential().getEmail()).ifPresent(u -> {
            throw new UserAlreadyExistsException("User with this email already exists");
        });

        userDao.findByPhoneNumber(userDto.getPhoneNumber()).ifPresent(u -> {
            throw new UserAlreadyExistsException("User with this phone number already exists");
        });

        String hashedPassword = passwordEncoder.encode(userDto.getCredential().getPassword());

        User user = modelMapper.map(userDto, User.class);
        user.getCredential().setPassword(hashedPassword);
        System.out.println("User: " + user);

        User savedUser = userDao.save(user);

        ShoppingCart cart = new ShoppingCart();
        cart.setUserId(user);
        shoppingCartsDao.save(cart);

        return modelMapper.map(savedUser, UserDetailsDto.class);
    }

    @RateLimit(type = "USER")
    @Override
    public UserDetailsDto registerAdmin(@Valid SaveUserDto userDto) {
        System.out.println("Admin UserDto: " + userDto);

        userDao.findByCredentialEmail(userDto.getCredential().getEmail()).ifPresent(u -> {
            throw new UserAlreadyExistsException("User with this email already exists");
        });

        userDao.findByPhoneNumber(userDto.getPhoneNumber()).ifPresent(u -> {
            throw new UserAlreadyExistsException("Admin with this phone number already exists");
        });

        String hashedPassword = passwordEncoder.encode(userDto.getCredential().getPassword());

        Admin admin = modelMapper.map(userDto, Admin.class);
        admin.getCredential().setPassword(hashedPassword);
        System.out.println("Admin: " + admin);

        userDao.save(admin);

        return modelMapper.map(admin, UserDetailsDto.class);
    }

    @Override
    public Map<String, String> loginUser(CredentialDto loginDto) {

        try{

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    loginDto.getEmail(),
                    loginDto.getPassword()
            );



            Authentication auth = authenticationManager.authenticate(authToken);



           LoggedUserDetails userDetails = (LoggedUserDetails) auth.getPrincipal();



        String accessToken = jwtService.generateToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails, expirationTimeInHours);

        RefreshTokenDto r = new RefreshTokenDto(refreshToken);
        refreshTokenService.save(r, userDetails);

        return Map.of("access_token", accessToken, "refresh_token", refreshToken);

    } catch (Exception e) {

        throw new LoginException("Invalid credentials");
    }
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, String> refreshToken(String authorizationHeader, String toString) {

        String refreshToken = authorizationHeader.substring("Bearer ".length());
        UsernamePasswordAuthenticationToken authenticationToken = null;
        try {
            authenticationToken = jwtService.parseToken(refreshToken);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        String username = authenticationToken.getName();
        User user = userDao.findByCredentialEmail(username).orElseThrow(()->new RuntimeException("user not found"));
        LoggedUserDetails loggedUserDetails = new LoggedUserDetails(user);
        String accessToken = jwtService.generateToken(loggedUserDetails);
        return Map.of("access_token", accessToken, "refresh_token", refreshToken);
    }

    @Override
    public AccessTokenValidationDto validateToken(AccessTokenValidationDto accessTokenValidationDto) {
            try {

                jwtService.isTokenValid(accessTokenValidationDto.getToken());

                if (revokedTokenService.isTokenRevoked(accessTokenValidationDto.getToken())) {
                    throw new JwtException("Token is revoked");
                }

                UUID userId = jwtService.extractUserId(accessTokenValidationDto.getToken());

                userDao.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));


                return accessTokenValidationDto;
            } catch (JwtException e) {
                throw new JwtException("Token is invalid");
            }
        }

    }

