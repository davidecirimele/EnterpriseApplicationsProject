package com.enterpriseapplicationsproject.ecommerce.controller;


import com.enterpriseapplicationsproject.ecommerce.data.entities.WishlistItem;
import com.enterpriseapplicationsproject.ecommerce.data.service.WishlistItemsService;
import com.enterpriseapplicationsproject.ecommerce.dto.WishlistItemDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wishlist-items-api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor //indica che il costruttore è generato automaticamente
@Slf4j
public class WishlistItemsController {

    private final WishlistItemsService wishlistItemsService;

    @GetMapping("/wishlist-items")
    public ResponseEntity<List<WishlistItemDto>> all() {
        return ResponseEntity.ok(wishlistItemsService.getAllSorted());
    }

    @GetMapping("/wishlist-items/{idWishlistItem}")
    public ResponseEntity<WishlistItem> getById(@PathVariable("idWishlistItem") Long id) {
        WishlistItem w = wishlistItemsService.getById(id);
        if(w == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(w);
    }

    @PostMapping("/wishlist-items")
    public ResponseEntity<WishlistItem> addItemToWishlist(@RequestBody Long wishlistId, WishlistItem wishlistItem) {
        return ResponseEntity.ok(wishlistItemsService.addItemToWishlist(wishlistId,wishlistItem));
    }
}
