package com.enterpriseapplicationsproject.ecommerce.data.service.impl;

import com.enterpriseapplicationsproject.ecommerce.config.security.JwtService;
import com.enterpriseapplicationsproject.ecommerce.config.security.LoggedUserDetails;
import com.enterpriseapplicationsproject.ecommerce.data.dao.UsersDao;
import com.enterpriseapplicationsproject.ecommerce.data.entities.User;
import com.enterpriseapplicationsproject.ecommerce.dto.LoginDto;
import com.enterpriseapplicationsproject.ecommerce.dto.SaveUserDto;
import org.springframework.security.core.Authentication;

import com.enterpriseapplicationsproject.ecommerce.data.service.AuthService;
import com.enterpriseapplicationsproject.ecommerce.dto.UserLoginDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements  AuthService{

    private final UsersDao userDao;
    private final ModelMapper modelMapper;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;



    @Override
    public SaveUserDto registerUser(SaveUserDto userDto) {
        System.out.println("UserDto: " + userDto);

        userDao.findByCredentialEmail(userDto.getCredentials().getEmail()).ifPresent(u -> {
            throw new IllegalArgumentException("User with this email already exists");
        });

        String hashedPassword = passwordEncoder.encode(userDto.getCredentials().getPassword());

        User user = modelMapper.map(userDto, User.class);
        user.getCredential().setPassword(hashedPassword);
        System.out.println("User: " + user);

        userDao.save(user);

        return modelMapper.map(user, SaveUserDto.class);
    }

    @Override
    public String loginUser(UserLoginDto loginDto) {
        System.out.println("LoginDto: " + loginDto);
        try{
            System.out.println("Attempting to create UsernamePasswordAuthenticationToken");
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    loginDto.getEmail(),
                    loginDto.getPassword()
            );
            System.out.println("UsernamePasswordAuthenticationToken created successfully");

            System.out.println("Attempting to authenticate");
            Authentication authentication = authenticationManager.authenticate(authToken);
            System.out.println("Authentication successful");
        Authentication auth =  authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
        System.out.println("Auth: " + auth);

        LoggedUserDetails userDetails = (LoggedUserDetails) auth.getPrincipal();

        String token = jwtService.generateToken(userDetails);

        return token;
    } catch (Exception e) {
            e.printStackTrace();
        throw new IllegalArgumentException("Invalid credentials");
    }
    }
}
