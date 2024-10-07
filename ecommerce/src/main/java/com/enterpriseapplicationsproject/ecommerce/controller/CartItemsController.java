package com.enterpriseapplicationsproject.ecommerce.controller;

import com.enterpriseapplicationsproject.ecommerce.data.dao.ShoppingCartsDao;
import com.enterpriseapplicationsproject.ecommerce.data.entities.CartItem;
import com.enterpriseapplicationsproject.ecommerce.data.entities.ShoppingCart;
import com.enterpriseapplicationsproject.ecommerce.data.service.CartItemsService;
import com.enterpriseapplicationsproject.ecommerce.data.service.UserService;
import com.enterpriseapplicationsproject.ecommerce.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("api/v1/shopping-cart/cart")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class CartItemsController {

    private final CartItemsService cartItemsService;

    @GetMapping("/get/items/{userId}/{cartId}")
    @PreAuthorize("#userId == authentication.principal.getId()")
    public ResponseEntity<List<CartItemDto>> getItems(@PathVariable UUID userId, @PathVariable Long cartId) {
        List<CartItemDto> items = cartItemsService.getCartItems(userId, cartId);
        return new ResponseEntity<>(items, HttpStatus.OK);
    }


    @PostMapping("/{userId}/{cartId}/{bookId}/insert")
    @PreAuthorize("#userId == authentication.principal.getId()")
    public ResponseEntity<CartItemDto> insertItem(@RequestBody QuantityCartItemDto quantityCartItemDto,@PathVariable Long cartId, @PathVariable Long bookId,  @PathVariable UUID userId) {
        System.out.println("INSERTED CI: "+quantityCartItemDto);
        CartItemDto insertedItem = cartItemsService.insert(quantityCartItemDto, userId, cartId, bookId);
        return new ResponseEntity<>(insertedItem, HttpStatus.CREATED);
    }

    @DeleteMapping("/{userId}/{cartId}/{itemId}/remove")
    @PreAuthorize("#userId == authentication.principal.getId()")
    public ResponseEntity<CartItemDto> removeItem(@PathVariable Long cartId, @PathVariable Long itemId,@PathVariable UUID userId) {

        boolean isRemoved = cartItemsService.delete(userId,cartId,itemId);

        if (!isRemoved) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("{userId}/{cartId}/{itemId}/edit-quantity")
    @PreAuthorize("#userId == authentication.principal.getId()")
    public ResponseEntity<CartItemDto> updateQuantity(@PathVariable UUID userId, @PathVariable Long cartId, @PathVariable Long itemId, @RequestBody QuantityCartItemDto quantityCartItemDto) {
        CartItemDto updatedItem = cartItemsService.updateQuantity(userId, cartId, itemId, quantityCartItemDto);
        return new ResponseEntity<>(updatedItem, HttpStatus.OK);
    }
}
