package com.enterpriseapplicationsproject.ecommerce.controller;

import com.enterpriseapplicationsproject.ecommerce.data.service.RefreshTokenService;
import com.enterpriseapplicationsproject.ecommerce.data.service.RevokedTokenService;
import com.enterpriseapplicationsproject.ecommerce.data.service.UserService;
import com.enterpriseapplicationsproject.ecommerce.dto.*;
import com.enterpriseapplicationsproject.ecommerce.exception.UserRegistrationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("api/v1/users")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final RevokedTokenService revokedTokenService;
    private final RefreshTokenService refreshTokenService;

    @GetMapping("/{id}")
    @PreAuthorize("#id == authentication.principal.getId()")
    public ResponseEntity<UserDetailsDto> getUserById(@PathVariable UUID id) {
        System.out.println("ROLE: "+userService.getUserRole(id));
        UserDetailsDto user = userService.getUserDetailsById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping(value = "{userId}/change-password", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("#userId == authentication.principal.getId()")
    public ResponseEntity<UserDto> updatePassword(@PathVariable UUID userId,@RequestBody PasswordUserDto userDto){
        try{
            UserDto updatedUser= userService.updatePassword(userId, userDto);
            String toRevoke = refreshTokenService.getTokenByUserId(userId).getToken();
            revokedTokenService.revokeToken(toRevoke);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);}
        catch(UserRegistrationException e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "{userId}/change-email", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("#userId == authentication.principal.getId()")
    public ResponseEntity<UserDto> updateEmail(@PathVariable UUID userId,@RequestBody EmailUserDto userDto){
        log.info("Received request for user/change-email");
        try{
            UserDto updatedUser= userService.updateEmail(userId, userDto);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);}
        catch(UserRegistrationException e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "{userId}/change-phone-number", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("#userId == authentication.principal.getId()")
    public ResponseEntity<UserDto> updatePhoneNumber(@PathVariable UUID userId,@RequestBody PhoneNumberUserDto userDto){
        log.info("Received request for user/change-phone-number");
        try{
            UserDto updatedUser= userService.updatePhoneNumber(userId, userDto);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);}
        catch(UserRegistrationException e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "{userId}/delete-account", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("#userId == authentication.principal.getId() or hasAuthority('ADMIN')")
    public ResponseEntity<UserDto> deleteAccount(@PathVariable UUID userId){
        try{
            userService.delete(userId);
            return new ResponseEntity<>(HttpStatus.OK);}
        catch(UserRegistrationException e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String accessToken, @RequestBody String refreshToken) {
        accessToken = accessToken.replace("Bearer ", "");
        revokedTokenService.revokeToken(accessToken);
        revokedTokenService.revokeToken(refreshToken);
        return ResponseEntity.ok().build();
    }

}
