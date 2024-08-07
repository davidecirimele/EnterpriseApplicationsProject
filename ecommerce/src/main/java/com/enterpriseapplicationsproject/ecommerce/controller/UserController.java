package com.enterpriseapplicationsproject.ecommerce.controller;

import com.enterpriseapplicationsproject.ecommerce.data.entities.User;
import com.enterpriseapplicationsproject.ecommerce.data.service.RefreshTokenService;
import com.enterpriseapplicationsproject.ecommerce.data.service.UserService;
import com.enterpriseapplicationsproject.ecommerce.dto.*;
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
@RequestMapping("api/v1/users")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final RefreshTokenService refreshTokenService;

    @GetMapping("/{id}")
    @PreAuthorize("#id == authentication.principal.getId()")
    public ResponseEntity<UserDto> getUserById(@PathVariable UUID id) {
        System.out.println("ROLE: "+userService.getUserRole(id));
        UserDto user = userService.getById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping(value = "/change-password", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("#userDto.userId == authentication.principal.getId()")
    public ResponseEntity<UserDto> updatePassword(@RequestBody PasswordUserDto userDto){
        try{
            UserDto updatedUser= userService.updatePassword(userDto);
            refreshTokenService.revokeRefreshTokenByUserId(userDto.getUserId());
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);}
        catch(UserRegistrationException e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/change-email", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("#userDto.id == authentication.principal.getId()")
    public ResponseEntity<UserDto> updateEmail(@RequestBody EmailUserDto userDto){
        try{
            UserDto updatedUser= userService.updateEmail(userDto);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);}
        catch(UserRegistrationException e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/change-phone-number", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("#userDto.userId == authentication.principal.getId()")
    public ResponseEntity<UserDto> updatePhoneNumber(@RequestBody PhoneNumberUserDto userDto){
        try{
            UserDto updatedUser= userService.updatePhoneNumber(userDto);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);}
        catch(UserRegistrationException e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
