package com.enterpriseapplicationsproject.ecommerce.controller;

import com.enterpriseapplicationsproject.ecommerce.data.service.ShoppingCartService;
import com.enterpriseapplicationsproject.ecommerce.data.service.UserService;
import com.enterpriseapplicationsproject.ecommerce.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/shopping_cart")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<ShoppingCartDto>> all() {
        List<ShoppingCartDto> shoppingCarts = shoppingCartService.getAll();
        return new ResponseEntity<>(shoppingCarts, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("#userId == authentication.principal.getId()")
    public ResponseEntity<ShoppingCartDto> get(@PathVariable UUID userId) {
        System.out.println("USER ID: "+userId);
        ShoppingCartDto shoppingCart = shoppingCartService.getByUserId(userId);
        return new ResponseEntity<>(shoppingCart, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("#userId.userId == authentication.principal.getId()")
    public ResponseEntity<Void> deleteShoppingCart(@RequestBody UserIdDto userId) {

        boolean isRemoved = shoppingCartService.delete(userId);
        if (!isRemoved) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/save")
    @PreAuthorize("#ShoppingCartDto.userId == authentication.principal.getId()")
    public ResponseEntity<ShoppingCartDto> saveCart(@RequestBody ShoppingCartDto ShoppingCartDto) {
        ShoppingCartDto savedCart = shoppingCartService.saveCart(ShoppingCartDto);
        return new ResponseEntity<>(savedCart, HttpStatus.OK);
    }
}


