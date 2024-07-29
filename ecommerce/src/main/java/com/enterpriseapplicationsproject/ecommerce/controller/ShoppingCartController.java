package com.enterpriseapplicationsproject.ecommerce.controller;

import com.enterpriseapplicationsproject.ecommerce.data.service.ShoppingCartService;
import com.enterpriseapplicationsproject.ecommerce.data.service.UserService;
import com.enterpriseapplicationsproject.ecommerce.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/shopping_cart")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    @GetMapping("/all")
    public ResponseEntity<List<ShoppingCartDto>> all() {
        List<ShoppingCartDto> shoppingCarts = shoppingCartService.getAll();
        return new ResponseEntity<>(shoppingCarts, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ShoppingCartDto> get(@PathVariable Long userId) {
        System.out.println("USER ID: "+userId);
        ShoppingCartDto shoppingCart = shoppingCartService.getByUserId(userId);
        return new ResponseEntity<>(shoppingCart, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteShoppingCart(@RequestBody UserIdDto userId) {

        boolean isRemoved = shoppingCartService.delete(userId);
        if (!isRemoved) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/create")
    public ResponseEntity<ShoppingCartDto> createCart(@RequestBody CreateShoppingCartDto createShoppingCartDto) {
        System.out.println("CREATED SC: "+createShoppingCartDto);
        ShoppingCartDto createdCart = shoppingCartService.createCart(createShoppingCartDto);
        return new ResponseEntity<>(createdCart, HttpStatus.CREATED);
    }

    @PutMapping("/save")
    public ResponseEntity<ShoppingCartDto> saveCart(@RequestBody SaveShoppingCartDto saveShoppingCartDto) {
        ShoppingCartDto savedCart = shoppingCartService.saveCart(saveShoppingCartDto);
        return new ResponseEntity<>(savedCart, HttpStatus.OK);
    }
}


