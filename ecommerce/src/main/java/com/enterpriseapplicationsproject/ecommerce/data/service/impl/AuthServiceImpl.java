package com.enterpriseapplicationsproject.ecommerce.data.service.impl;

import com.enterpriseapplicationsproject.ecommerce.config.security.JwtService;
import com.enterpriseapplicationsproject.ecommerce.config.security.LoggedUserDetails;
import com.enterpriseapplicationsproject.ecommerce.data.dao.ShoppingCartsDao;
import com.enterpriseapplicationsproject.ecommerce.data.dao.UsersDao;
import com.enterpriseapplicationsproject.ecommerce.data.entities.Admin;
import com.enterpriseapplicationsproject.ecommerce.data.entities.RefreshToken;
import com.enterpriseapplicationsproject.ecommerce.data.entities.ShoppingCart;
import com.enterpriseapplicationsproject.ecommerce.data.entities.User;
import com.enterpriseapplicationsproject.ecommerce.data.service.RefreshTokenService;
import com.enterpriseapplicationsproject.ecommerce.dto.LoginDto;
import com.enterpriseapplicationsproject.ecommerce.dto.SaveUserDto;
import com.enterpriseapplicationsproject.ecommerce.dto.security.RefreshTokenDto;
import com.enterpriseapplicationsproject.ecommerce.exception.UserAlreadyExistsException;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;

import com.enterpriseapplicationsproject.ecommerce.data.service.AuthService;
import com.enterpriseapplicationsproject.ecommerce.dto.UserLoginDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Map;

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

    private final Integer expirationTimeInHours = 96;


    @Override
    public SaveUserDto registerUser( @Valid SaveUserDto userDto) {
        System.out.println("UserDto: " + userDto);

        userDao.findByCredentialEmail(userDto.getCredentials().getEmail()).ifPresent(u -> {
            throw new UserAlreadyExistsException("User with this email already exists");
        });

        String hashedPassword = passwordEncoder.encode(userDto.getCredentials().getPassword());

        User user = modelMapper.map(userDto, User.class);
        user.getCredential().setPassword(hashedPassword);
        System.out.println("User: " + user);

        userDao.save(user);

        System.out.println("CREATE SHOPPING CART USER ID: "+user.getId());
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(user); // Imposta l'utente trovato nel carrello
        shoppingCart.setCartItems(new ArrayList<>()); // Inizializza la lista degli articoli del carrello

        shoppingCartsDao.save(shoppingCart);

        return modelMapper.map(user, SaveUserDto.class);
    }

    @Override
    public SaveUserDto registerAdmin(SaveUserDto userDto) {
        System.out.println("Admin UserDto: " + userDto);

        userDao.findByCredentialEmail(userDto.getCredentials().getEmail()).ifPresent(u -> {
            throw new IllegalArgumentException("Admin with this email already exists");
        });

        String hashedPassword = passwordEncoder.encode(userDto.getCredentials().getPassword());

        Admin admin = modelMapper.map(userDto, Admin.class);
        admin.getCredential().setPassword(hashedPassword);
        System.out.println("Admin: " + admin);

        userDao.save(admin);

        return modelMapper.map(admin, SaveUserDto.class);
    }

    @Override
    public Map<String, String> loginUser(UserLoginDto loginDto) {
        System.out.println("LoginDto: " + loginDto);
        try{
            System.out.println("Attempting to create UsernamePasswordAuthenticationToken");
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    loginDto.getEmail(),
                    loginDto.getPassword()
            );
            System.out.println("UsernamePasswordAuthenticationToken created successfully");

            System.out.println("Attempting to authenticate");
            Authentication auth = authenticationManager.authenticate(authToken);
            System.out.println("Authentication successful");


           LoggedUserDetails userDetails = (LoggedUserDetails) auth.getPrincipal();



        String accessToken = jwtService.generateToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails, expirationTimeInHours);

        RefreshTokenDto r = new RefreshTokenDto(refreshToken);
        refreshTokenService.save(r, userDetails);

        return Map.of("access_token", accessToken, "refresh_token", refreshToken);

    } catch (Exception e) {
            e.printStackTrace();
        throw new IllegalArgumentException("Invalid credentials");
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
}
