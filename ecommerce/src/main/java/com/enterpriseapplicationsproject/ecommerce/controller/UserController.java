package com.enterpriseapplicationsproject.ecommerce.controller;

import com.enterpriseapplicationsproject.ecommerce.data.entities.User;
import com.enterpriseapplicationsproject.ecommerce.data.service.UserService;
import com.enterpriseapplicationsproject.ecommerce.dto.*;
import com.enterpriseapplicationsproject.ecommerce.exception.UserRegistrationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/users-api", produces = "application/json")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping(produces = "application/json",  path = "/all")
    public ResponseEntity<List<UserDto>> all() {
        List<UserDto> users = userService.getAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{id}")
//    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<UserDto> getUserById(@PathVariable UUID id) {
        UserDto user = userService.getById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping(value = "/change-password", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> updatePassword(@RequestBody PasswordUserDto userDto){
        try{
            UserDto updatedUser= userService.updatePassword(userDto);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);}
        catch(UserRegistrationException e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/change-email", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> updateEmail(@RequestBody EmailUserDto userDto){
        try{
            UserDto updatedUser= userService.updateEmail(userDto);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);}
        catch(UserRegistrationException e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/change-phone-number", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> updatePhoneNumber(@RequestBody PhoneNumberUserDto userDto){
        try{
            UserDto updatedUser= userService.updatePhoneNumber(userDto);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);}
        catch(UserRegistrationException e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
