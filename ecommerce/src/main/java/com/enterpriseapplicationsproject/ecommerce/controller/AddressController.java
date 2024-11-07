package com.enterpriseapplicationsproject.ecommerce.controller;

import com.enterpriseapplicationsproject.ecommerce.config.security.RateLimit;
import com.enterpriseapplicationsproject.ecommerce.data.entities.User;
import com.enterpriseapplicationsproject.ecommerce.data.service.AddressService;
import com.enterpriseapplicationsproject.ecommerce.data.service.UserService;
import com.enterpriseapplicationsproject.ecommerce.dto.*;
import com.enterpriseapplicationsproject.ecommerce.exception.AddressNotFoundException;
import com.enterpriseapplicationsproject.ecommerce.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("api/v1/addresses")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    private final UserService userService;

    @RateLimit(type ="USER")
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AddressDto>> getAddresses() {

        List<AddressDto> addresses = addressService.getAddresses();

        if (addresses.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(addresses, HttpStatus.OK);

    }

    @RateLimit(type ="USER")
    @GetMapping("/{id}")
    @PreAuthorize("#id == authentication.principal.getId() or hasRole('ADMIN')")
    public ResponseEntity<List<AddressDto>> getValidAddressesByUserId(@PathVariable UUID id) {
        User user = userService.getUserById(id);

        List<AddressDto> addresses = addressService.getValidAddressesByUserId(user.getId());
        if (addresses.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(addresses, HttpStatus.OK);
    }

    @RateLimit(type ="USER")
    @GetMapping("/{id}/default")
    @PreAuthorize("#id == authentication.principal.getId() or hasRole('ADMIN')")
    public ResponseEntity<AddressDto> getDefaultAddressByUserId(@PathVariable UUID id) {

        User user = userService.getUserById(id);

        AddressDto address = addressService.getAddressByUserIdAndDefaultTrue(user.getId());

        return new ResponseEntity<>(address, HttpStatus.OK);

    }

    @RateLimit(type ="USER")
    @GetMapping("/{userId}/{id}")
    @PreAuthorize("#userId == authentication.principal.getId() or hasRole('ADMIN')")
    public ResponseEntity<AddressDto> getValidAddressByUserId(@PathVariable UUID userId, @PathVariable Long id) {

        AddressDto address = addressService.getAddressById(userId, id);

        return new ResponseEntity<>(address, HttpStatus.OK);
    }

    @RateLimit(type ="USER")
    @GetMapping("/all/{id}")
    @PreAuthorize("#id == authentication.principal.getId() or hasRole('ADMIN')")
    public ResponseEntity<List<AddressDto>> getAddressByUserId(@PathVariable UUID id) {
        log.info("Received request for addresses/all/{id}");
        User user = userService.getUserById(id);

        List<AddressDto> addresses = addressService.getAddressesByUserId(user.getId());
        return new ResponseEntity<>(addresses, HttpStatus.OK);
    }

    @RateLimit(type ="USER")
    @PostMapping(consumes = "application/json", path = "/{userId}/insert-address")
    @PreAuthorize("#userId == authentication.principal.getId() or hasRole('ADMIN')")
    public ResponseEntity<AddressDto> insertAddress(@PathVariable UUID userId, @RequestBody SaveAddressDto addressDto){
        log.info("Received request for addresses/{userId}/insert-address");

        AddressDto addedAddress = addressService.insertAddress(userId, addressDto);
        return new ResponseEntity<>(addedAddress, HttpStatus.CREATED);
    }


    @RateLimit(type ="USER")
    @DeleteMapping("{userId}/{addressId}/delete")
    @PreAuthorize("#userId == authentication.principal.getId() or hasRole('ADMIN')")
    public ResponseEntity<Boolean> deleteAddress(@PathVariable UUID userId, @PathVariable Long addressId) {
        log.info("Received request for addresses/{addressId}/delete");

        addressService.deleteAddress(userId, addressId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("{userId}/{addressId}/update-default")
    @PreAuthorize("#userId == authentication.principal.getId() or hasRole('ADMIN')")
    public ResponseEntity<AddressDto> updateDefaultAddress(@PathVariable UUID userId, @PathVariable Long addressId) {
        log.info("Received request for addresses/{addressId}/update-default");
        AddressDto updatedAddress= addressService.updateDefaultAddress(userId, addressId);
        return new ResponseEntity<>(updatedAddress, HttpStatus.OK);
    }

    @RateLimit(type ="USER")
    @PutMapping("/{userId}/{addressId}/edit-address")
    @PreAuthorize("#userId == authentication.principal.getId() or hasRole('ADMIN')")
    public ResponseEntity<AddressDto> updateAddress(@PathVariable UUID userId, @PathVariable Long addressId, @RequestBody SaveAddressDto addressDto) {
        log.info("Received request for addresses/{userId}/{addressId}/edit-address");

        AddressDto updatedAddress = addressService.updateAddress(userId, addressId, addressDto);
        return new ResponseEntity<>(updatedAddress, HttpStatus.OK);
    }
}
