package com.enterpriseapplicationsproject.ecommerce.controller;


import com.enterpriseapplicationsproject.ecommerce.data.entities.WishlistItem;
import com.enterpriseapplicationsproject.ecommerce.data.service.WishlistItemsService;
import com.enterpriseapplicationsproject.ecommerce.dto.WishlistItemDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/wishlist-items", produces = "application/json") //indica che la classe risponde a richieste REST sulla base path "/api/v1/wishlist-items" e che produce risposte in formato JSON
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor //indica che il costruttore è generato automaticamente
@Slf4j
public class WishlistItemsController {

    private final WishlistItemsService wishlistItemsService;

    @GetMapping(consumes = "application/json", path = "/get/all")
    public ResponseEntity<List<WishlistItemDto>> all() {
        List<WishlistItemDto> wishlistItems = wishlistItemsService.getAllSorted();
        if (wishlistItems == null || wishlistItems.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(wishlistItems, HttpStatus.OK);
    }

    @GetMapping(consumes = "application/json", path = "/get/{idWishlistItem}")
    public ResponseEntity<WishlistItem> getById(@PathVariable("idWishlistItem") Long id) {
        WishlistItem w = wishlistItemsService.getById(id);
        if (w == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(w, HttpStatus.OK);
    }

    @PostMapping(consumes = "application/json", path = "/add/{wishlistId}")
    public ResponseEntity<WishlistItem> addItemToWishlist(@RequestBody Long wishlistId, WishlistItem wishlistItem) {
        WishlistItem w = wishlistItemsService.addItemToWishlist(wishlistId, wishlistItem);
        if (w == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(w, HttpStatus.OK);
    }

    @DeleteMapping(path = "/delete/{idWishlistItem}")
    public HttpStatus delete(@RequestBody Long wishlistId, WishlistItem wishlistItem ) {
        wishlistItemsService.deleteByIdAndWishlistId(wishlistItem.getId(), wishlistId);
        return HttpStatus.OK;
    }
}
