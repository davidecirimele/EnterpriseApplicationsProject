package com.enterpriseapplicationsproject.ecommerce.controller;

import com.enterpriseapplicationsproject.ecommerce.data.entities.User;
import com.enterpriseapplicationsproject.ecommerce.data.service.AddressService;
import com.enterpriseapplicationsproject.ecommerce.data.service.UserService;
import com.enterpriseapplicationsproject.ecommerce.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/addresses")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    private final UserService userService;

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<AddressDto>> getAddresses() {
        List<AddressDto> addresses = addressService.getAddresses();
        return new ResponseEntity<>(addresses, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("#id == authentication.principal.getId()")
    public ResponseEntity<List<AddressDto>> getValidAddressesByUserId(@PathVariable UUID id) {
        User user = userService.getUserById(id);
        if (user != null) {
            List<AddressDto> addresses = addressService.getValidAddressesByUserId(user.getId());
            return new ResponseEntity<>(addresses, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all/{id}")
    @PreAuthorize("#id == authentication.principal.getId() or hasAuthority('ADMIN')")
    public ResponseEntity<List<AddressDto>> getAddressByUserId(@PathVariable UUID id) {

        User user = userService.getUserById(id);
        if (user != null) {
            List<AddressDto> addresses = addressService.getAddressesByUserId(user.getId());
            return new ResponseEntity<>(addresses, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(consumes = "application/json", path = "/{userId}/insert-address")
    @PreAuthorize("#userId == authentication.principal.getId()")
    public ResponseEntity<SaveAddressDto> insertAddress(@PathVariable UUID userId, @RequestBody SaveAddressDto addressDto){
        System.out.println("Received AddressDto: " + addressDto);

        SaveAddressDto addedAddress = addressService.insertAddress(userId, addressDto);
        return new ResponseEntity<>(addedAddress, HttpStatus.CREATED);
    }


    @DeleteMapping("/{addressId}/delete")
    @PreAuthorize("#userId.userId == authentication.principal.getId()")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long addressId, @RequestBody UserIdDto userId) {

        boolean isRemoved = addressService.deleteAddress(addressId);
        if (!isRemoved) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{addressId}/update-default")
    @PreAuthorize("#userId.userId == authentication.principal.getId()")
    public ResponseEntity<AddressDto> updateDefaultAddress(@PathVariable Long addressId, @RequestBody UserIdDto userId) {
        AddressDto updatedAddress= addressService.updateDefaultAddress(addressId);
        return new ResponseEntity<>(updatedAddress, HttpStatus.OK);
    }

    @PutMapping("/{userId}/{addressId}/edit-address")
    @PreAuthorize("#userId == authentication.principal.getId()")
    public ResponseEntity<AddressDto> updateDefaultAddress(@PathVariable UUID userId, @PathVariable Long addressId, @RequestBody SaveAddressDto addressDto) {


        AddressDto updatedAddress = addressService.updateAddress(addressId, addressDto);
        return new ResponseEntity<>(updatedAddress, HttpStatus.OK);
    }
}
