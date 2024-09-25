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

@Slf4j
@RestController
@RequestMapping("api/v1/cart")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class CartItemsController {

    private final CartItemsService cartItemsService;

    private final ShoppingCartsDao shoppingCartsDao;

    //@GetMapping("/{id}")
//    @PreAuthorize("hasAuthority('ROLE_USER')")
    //public ResponseEntity<CartItemDto> getUserById(@PathVariable long id) {
        //List<ProductDto> cartitems = cartItemsService.getProductByCartId(id);
        //return new ResponseEntity<>(cartitems, HttpStatus.OK);
    //}

    @GetMapping("/shopping_cart/{cartId}")
    @PreAuthorize("#userId.userId == authentication.principal.getId()")
    public ResponseEntity<List<CartItemDto>> getItems(@PathVariable Long cartId, @RequestBody UserIdDto userId) {
        List<CartItemDto> items = cartItemsService.getCartItemsByCartId(cartId);
        return new ResponseEntity<>(items, HttpStatus.OK);
    }


    @PostMapping("/{bookId}/insert")
    @PreAuthorize("#insertCartItemDto.userId.userId == authentication.principal.getId()")
    public ResponseEntity<CartItemDto> insertItem(@RequestBody InsertCartItemDto insertCartItemDto,@PathVariable Long bookId) {
        System.out.println("INSERTED CI: "+insertCartItemDto);
        CartItemDto insertedItem = cartItemsService.insert(insertCartItemDto, bookId);
        return new ResponseEntity<>(insertedItem, HttpStatus.CREATED);
    }

    @DeleteMapping("/{itemId}/remove")
    @PreAuthorize("#id.userId == authentication.principal.getId()")
    public ResponseEntity<CartItemDto> removeItem(@PathVariable Long itemId,@RequestBody UserIdDto id) {
        System.out.println("REMOVE CI: "+id);

        boolean isRemoved = cartItemsService.delete(itemId);

        if (!isRemoved) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{itemId}/edit-quantity")
    @PreAuthorize("#quantityCartItemDto.userId.userId == authentication.principal.getId()")
    public ResponseEntity<CartItemDto> updateQuantity(@PathVariable Long itemId, @RequestBody QuantityCartItemDto quantityCartItemDto) {
        CartItemDto updatedItem = cartItemsService.updateQuantity(itemId, quantityCartItemDto);
        return new ResponseEntity<>(updatedItem, HttpStatus.OK);
    }
}
