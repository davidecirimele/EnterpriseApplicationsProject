package com.enterpriseapplicationsproject.ecommerce.controller;

import com.enterpriseapplicationsproject.ecommerce.data.entities.User;
import com.enterpriseapplicationsproject.ecommerce.data.service.AddressService;
import com.enterpriseapplicationsproject.ecommerce.data.service.UserService;
import com.enterpriseapplicationsproject.ecommerce.dto.*;
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

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AddressDto>> getAddresses() {
        log.info("Received request for addresses/all");
        List<AddressDto> addresses = addressService.getAddresses();
        return new ResponseEntity<>(addresses, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("#id == authentication.principal.getId() or hasRole('ADMIN')")
    public ResponseEntity<List<AddressDto>> getValidAddressesByUserId(@PathVariable UUID id) {
        log.info("Received request for addresses/{id}");
        User user = userService.getUserById(id);
        if (user != null) {
            List<AddressDto> addresses = addressService.getValidAddressesByUserId(user.getId());
            return new ResponseEntity<>(addresses, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}/default")
    @PreAuthorize("#id == authentication.principal.getId() or hasRole('ADMIN')")
    public ResponseEntity<AddressDto> getDefaultAddressByUserId(@PathVariable UUID id) {
        log.info("Received request for addresses/{id}/default");
        User user = userService.getUserById(id);
        if (user != null) {
            AddressDto address = addressService.getAddressByUserIdAndDefaultTrue(user.getId());
            return new ResponseEntity<>(address, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{userId}/{id}")
    @PreAuthorize("#userId == authentication.principal.getId() or hasRole('ADMIN')")
    public ResponseEntity<AddressDto> getValidAddressByUserId(@PathVariable UUID userId, @PathVariable Long id) {
        log.info("Received request for addresses/{userId}/{id}");
        User user = userService.getUserById(userId);
        if (user != null) {
            AddressDto address = addressService.getAddressById(id);
            return new ResponseEntity<>(address, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all/{id}")
    @PreAuthorize("#id == authentication.principal.getId() or hasRole('ADMIN')")
    public ResponseEntity<List<AddressDto>> getAddressByUserId(@PathVariable UUID id) {
        log.info("Received request for addresses/all/{id}");
        User user = userService.getUserById(id);
        if (user != null) {
            List<AddressDto> addresses = addressService.getAddressesByUserId(user.getId());
            return new ResponseEntity<>(addresses, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(consumes = "application/json", path = "/{userId}/insert-address")
    @PreAuthorize("#userId == authentication.principal.getId() or hasRole('ADMIN')")
    public ResponseEntity<AddressDto> insertAddress(@PathVariable UUID userId, @RequestBody SaveAddressDto addressDto){
        log.info("Received request for addresses/{userId}/insert-address");

        AddressDto addedAddress = addressService.insertAddress(userId, addressDto);
        return new ResponseEntity<>(addedAddress, HttpStatus.CREATED);
    }


    @DeleteMapping("/{addressId}/delete")
    @PreAuthorize("#userId.userId == authentication.principal.getId() or hasRole('ADMIN')")
    public ResponseEntity<Boolean> deleteAddress(@PathVariable Long addressId, @RequestBody UserIdDto userId) {
        log.info("Received request for addresses/{addressId}/delete");
        boolean isRemoved = addressService.deleteAddress(addressId);
        if (!isRemoved) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{addressId}/update-default")
    @PreAuthorize("#userId.userId == authentication.principal.getId() or hasRole('ADMIN')")
    public ResponseEntity<AddressDto> updateDefaultAddress(@PathVariable Long addressId, @RequestBody UserIdDto userId) {
        log.info("Received request for addresses/{addressId}/update-default");
        AddressDto updatedAddress= addressService.updateDefaultAddress(addressId);
        return new ResponseEntity<>(updatedAddress, HttpStatus.OK);
    }

    @PutMapping("/{userId}/{addressId}/edit-address")
    @PreAuthorize("#userId == authentication.principal.getId() or hasRole('ADMIN')")
    public ResponseEntity<AddressDto> updateAddress(@PathVariable UUID userId, @PathVariable Long addressId, @RequestBody SaveAddressDto addressDto) {
        log.info("Received request for addresses/{userId}/{addressId}/edit-address");

        AddressDto updatedAddress = addressService.updateAddress(addressId, addressDto);
        return new ResponseEntity<>(updatedAddress, HttpStatus.OK);
    }
}
