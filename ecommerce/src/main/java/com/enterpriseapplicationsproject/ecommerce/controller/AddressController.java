package com.enterpriseapplicationsproject.ecommerce.controller;

import com.enterpriseapplicationsproject.ecommerce.data.entities.User;
import com.enterpriseapplicationsproject.ecommerce.data.service.AddressService;
import com.enterpriseapplicationsproject.ecommerce.data.service.UserService;
import com.enterpriseapplicationsproject.ecommerce.dto.AddressDto;
import com.enterpriseapplicationsproject.ecommerce.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/addresses")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    private final UserService userService;

    @GetMapping("/all")
    public ResponseEntity<List<AddressDto>> getAddresses() {
        List<AddressDto> addresses = addressService.getAddresses();
        return new ResponseEntity<>(addresses, HttpStatus.OK);
    }

    @GetMapping("/{id}")
//    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<List<AddressDto>> getAddressByUserId(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user != null) {
            List<AddressDto> addresses = addressService.getAddressesByUserId(user.getId());
            return new ResponseEntity<>(addresses, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/insert")
    public ResponseEntity<AddressDto> insertAddress(@RequestBody AddressDto addressDto){
        AddressDto addedAddress = addressService.insertAddress(addressDto);
        return new ResponseEntity<>(addedAddress, HttpStatus.CREATED);
    }
}
