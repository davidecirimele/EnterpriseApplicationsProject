package com.enterpriseapplicationsproject.ecommerce.controller;

import com.enterpriseapplicationsproject.ecommerce.data.entities.CartItem;
import com.enterpriseapplicationsproject.ecommerce.data.service.CartItemsService;
import com.enterpriseapplicationsproject.ecommerce.data.service.UserService;
import com.enterpriseapplicationsproject.ecommerce.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/cart")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class CartItemsController {

    private final CartItemsService cartItemsService;

    //@GetMapping("/{id}")
//    @PreAuthorize("hasAuthority('ROLE_USER')")
    //public ResponseEntity<CartItemDto> getUserById(@PathVariable long id) {
        //List<ProductDto> cartitems = cartItemsService.getProductByCartId(id);
        //return new ResponseEntity<>(cartitems, HttpStatus.OK);
    //}

    @PostMapping("/insert")
    @PreAuthorize("#insertCartItemDto.userId.userId == authentication.principal.getId()")
    public ResponseEntity<CartItemDto> insertItem(@RequestBody InsertCartItemDto insertCartItemDto) {
        System.out.println("INSERTED CI: "+insertCartItemDto);
        CartItemDto insertedItem = cartItemsService.insert(insertCartItemDto);
        return new ResponseEntity<>(insertedItem, HttpStatus.CREATED);
    }

    @DeleteMapping("/remove")
    @PreAuthorize("#id.userId.userId == authentication.principal.getId()")
    public ResponseEntity<CartItemDto> removeItem(@RequestBody CartItemIdDto id) {
        System.out.println("REMOVE CI: "+id);

        boolean isRemoved = cartItemsService.delete(id);

        if (!isRemoved) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/edit-quantity")
    @PreAuthorize("#quantityCartItemDto.userId.userId == authentication.principal.getId()")
    public ResponseEntity<CartItemDto> updateQuantity(@RequestBody QuantityCartItemDto quantityCartItemDto) {
        CartItemDto updatedItem = cartItemsService.updateQuantity(quantityCartItemDto);
        return new ResponseEntity<>(updatedItem, HttpStatus.OK);
    }
}
