package com.enterpriseapplicationsproject.ecommerce.controller;

import com.enterpriseapplicationsproject.ecommerce.data.service.ShoppingCartService;
import com.enterpriseapplicationsproject.ecommerce.data.service.UserService;
import com.enterpriseapplicationsproject.ecommerce.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("api/v1/shopping-cart")
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

    @GetMapping("/get/{userId}")
    @PreAuthorize("#userId == authentication.principal.getId()")
    public ResponseEntity<ShoppingCartDto> get(@PathVariable UUID userId) {
        ShoppingCartDto shoppingCart = shoppingCartService.getByUserId(userId);
        return new ResponseEntity<>(shoppingCart, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}/{cartId}/clear")
    @PreAuthorize("#userId == authentication.principal.getId()")
    public ResponseEntity<Void> deleteShoppingCart(@PathVariable UUID userId, @PathVariable Long cartId) {

        boolean isRemoved = shoppingCartService.delete(userId, cartId);
        if (!isRemoved) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /*@PutMapping("/save")
    @PreAuthorize("#ShoppingCartDto.userId == authentication.principal.getId()")
    public ResponseEntity<ShoppingCartDto> saveCart(@RequestBody ShoppingCartDto ShoppingCartDto) {
        ShoppingCartDto savedCart = shoppingCartService.saveCart(ShoppingCartDto);
        return new ResponseEntity<>(savedCart, HttpStatus.OK);
    }*/
}


