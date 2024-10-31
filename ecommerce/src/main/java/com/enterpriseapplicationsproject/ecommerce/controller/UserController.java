package com.enterpriseapplicationsproject.ecommerce.controller;

import com.enterpriseapplicationsproject.ecommerce.config.security.RateLimit;
import com.enterpriseapplicationsproject.ecommerce.data.service.RefreshTokenService;
import com.enterpriseapplicationsproject.ecommerce.data.service.RevokedTokenService;
import com.enterpriseapplicationsproject.ecommerce.data.service.UserService;
import com.enterpriseapplicationsproject.ecommerce.dto.*;
import com.enterpriseapplicationsproject.ecommerce.exception.UserRegistrationException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @RateLimit(type ="USER")
    @GetMapping("/{id}")
    @PreAuthorize("#id == authentication.principal.getId() or hasRole('ADMIN')")
    public ResponseEntity<UserDetailsDto> getUserById(@PathVariable UUID id) {
        System.out.println("ROLE: "+userService.getUserRole(id));
        UserDetailsDto user = userService.getUserDetailsById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @RateLimit(type ="USER")
    @GetMapping("/all-users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDetailsDto>> allUsers() {
        List<UserDetailsDto> users = userService.getAllDto();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }


    @RateLimit(type ="USER")
    @PutMapping(value = "{userId}/change-password", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("#userId == authentication.principal.getId()")
    public ResponseEntity<UserDto> updatePassword(@PathVariable UUID userId,@Valid @RequestBody PasswordUserDto userDto){
        try{
            UserDto updatedUser= userService.updatePassword(userId, userDto);
            String toRevoke = refreshTokenService.getTokenByUserId(userId).getToken();
            revokedTokenService.revokeToken(toRevoke);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);}
        catch(UserRegistrationException e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RateLimit(type ="USER")
    @PutMapping(value = "{userId}/change-email", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("#userId == authentication.principal.getId()")
    public ResponseEntity<UserDto> updateEmail(@PathVariable UUID userId, @Valid @RequestBody EmailUserDto userDto){
        log.info("Received request for user/change-email");
        try{
            log.info("Email is valid: "+userDto.getNewEmail());
            UserDto updatedUser= userService.updateEmail(userId, userDto);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);}
        catch(UserRegistrationException e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RateLimit(type ="USER")
    @PutMapping(value = "{userId}/change-phone-number", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("#userId == authentication.principal.getId()")
    public ResponseEntity<UserDto> updatePhoneNumber(@PathVariable UUID userId,@Valid @RequestBody PhoneNumberUserDto userDto){
        log.info("Received request for user/change-phone-number");
        try{
            UserDto updatedUser= userService.updatePhoneNumber(userId, userDto);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);}
        catch(UserRegistrationException e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @RateLimit(type ="USER")
    @DeleteMapping(value = "{userId}/delete-account", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("#userId == authentication.principal.getId() or hasRole('ADMIN')")
    public ResponseEntity<UserDto> deleteAccount(@PathVariable UUID userId){
        try{
            userService.delete(userId);
            return new ResponseEntity<>(HttpStatus.OK);}
        catch(UserRegistrationException e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RateLimit(type ="USER")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String accessToken, @RequestBody String refreshToken) {
        accessToken = accessToken.replace("Bearer ", "");
        revokedTokenService.revokeToken(accessToken);
        revokedTokenService.revokeToken(refreshToken);
        return ResponseEntity.ok().build();
    }

}
