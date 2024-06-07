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

    @GetMapping(consumes = "application/json", path = "/getAll")
    public ResponseEntity<List<WishlistItemDto>> all() {
        List<WishlistItemDto> wishlistItems = wishlistItemsService.getAllSorted();
        if (wishlistItems == null || wishlistItems.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(wishlistItems, HttpStatus.OK);
    }

    @GetMapping(consumes = "application/json", path = "/get/{idWishlistItem}")
    public ResponseEntity<WishlistItemDto> getById(@PathVariable("idWishlistItem") Long id) {
        WishlistItemDto wi = wishlistItemsService.getById(id);
        if (wi == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(wi, HttpStatus.OK);
    }

    @PostMapping(consumes = "application/json", path = "/add/{wishlistId}")
    public ResponseEntity<WishlistItemDto> addItemToWishlist(@RequestBody Long wishlistId, WishlistItem wishlistItem) {
        WishlistItemDto wi = wishlistItemsService.addItemToWishlist(wishlistId, wishlistItem);
        if (wi == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(wi, HttpStatus.OK);
    }

    @DeleteMapping(consumes = "application/json", path = "/delete/{idWishlistItem}")
    public ResponseEntity<WishlistItemDto> delete(@RequestBody Long wishlistId, WishlistItem wishlistItem ) {
        WishlistItemDto wi = wishlistItemsService.deleteByIdAndWishlistId(wishlistItem.getId(), wishlistId);
        if (wi == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(wi, HttpStatus.OK);
    }
}
