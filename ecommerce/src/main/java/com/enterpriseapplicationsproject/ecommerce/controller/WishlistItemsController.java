package com.enterpriseapplicationsproject.ecommerce.controller;


import com.enterpriseapplicationsproject.ecommerce.data.entities.WishlistItem;
import com.enterpriseapplicationsproject.ecommerce.data.service.WishlistItemsService;
import com.enterpriseapplicationsproject.ecommerce.dto.WishlistItemDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/wishlist-items", produces = "application/json") //indica che la classe risponde a richieste REST sulla base path "/api/v1/wishlist-items" e che produce risposte in formato JSON
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor //indica che il costruttore è generato automaticamente
@Slf4j
public class WishlistItemsController {

    private final WishlistItemsService wishlistItemsService;

    @GetMapping(path = "/getAll")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<WishlistItemDto>> getAllSorted() {
        List<WishlistItemDto> wishlistItems = wishlistItemsService.getAllSorted();
        if (wishlistItems.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(wishlistItems, HttpStatus.OK);
    }

    @GetMapping(consumes = "application/json", path = "/getByWishlistId/{idWishlist}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<WishlistItemDto>> getByWishlistId(@PathVariable Long idWishlist) {
        List<WishlistItemDto> wishlistItems = wishlistItemsService.getItemsByWishlistId(idWishlist);
        if (wishlistItems == null || wishlistItems.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(wishlistItems, HttpStatus.OK);
    }

    @GetMapping(consumes = "application/json", path = "/get/{idWishlistItem}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<WishlistItemDto> getById(@PathVariable Long idWishlistItem) {
        System.out.println("idWishlistItem: " + idWishlistItem);
        WishlistItemDto wi = null;
        try {
            wi = wishlistItemsService.getById(idWishlistItem);
            if (wi == null) {
                System.out.println("wi is null");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        System.out.println("wi: " + wi.toString());
        return new ResponseEntity<>(wi, HttpStatus.OK);
    }

    @PostMapping(consumes = "application/json", path = "/add")
    @PreAuthorize("#wishlistItem.getWishlist().getUserId() == authentication.principal.getId()")
    public ResponseEntity<WishlistItemDto> addItemToWishlist(@RequestBody  WishlistItem wishlistItem) {
        WishlistItemDto wi = wishlistItemsService.addItemToWishlist(wishlistItem);
        if (wi == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(wi, HttpStatus.OK);
    }

    @DeleteMapping(consumes = "application/json", path = "/delete/{idWishlistItem}")
    @PreAuthorize("#wishlistItem.getWishlist().getUserId() == authentication.principal.getId() or hasRole('ADMIN')")
    public ResponseEntity<WishlistItemDto> deleteItemToWishlist(@RequestBody Long wishlistId, WishlistItem wishlistItem ) {
        WishlistItemDto wi = wishlistItemsService.deleteItemById(wishlistItem);
        if (wi == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(wi, HttpStatus.OK);
    }
}
