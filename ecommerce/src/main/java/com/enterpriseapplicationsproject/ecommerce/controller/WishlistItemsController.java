package com.enterpriseapplicationsproject.ecommerce.controller;


import com.enterpriseapplicationsproject.ecommerce.config.security.RateLimit;
import com.enterpriseapplicationsproject.ecommerce.config.security.RateLimitType;
import com.enterpriseapplicationsproject.ecommerce.data.entities.Wishlist;
import com.enterpriseapplicationsproject.ecommerce.data.entities.WishlistItem;
import com.enterpriseapplicationsproject.ecommerce.data.service.WishlistItemsService;
import com.enterpriseapplicationsproject.ecommerce.dto.UserIdDto;
import com.enterpriseapplicationsproject.ecommerce.dto.WishlistDto;
import com.enterpriseapplicationsproject.ecommerce.dto.WishlistItemDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/v1/wishlist-items") //indica che la classe risponde a richieste REST sulla base path "/api/v1/wishlist-items" e che produce risposte in formato JSON
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor //indica che il costruttore è generato automaticamente
@Slf4j
public class WishlistItemsController {

    private final WishlistItemsService wishlistItemsService;

    @RateLimit(type = "USER")
    @GetMapping(path = "/getAll")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<WishlistItemDto>> getAllSorted() {
        List<WishlistItemDto> wishlistItems = wishlistItemsService.getAllSorted();
        if (wishlistItems.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(wishlistItems, HttpStatus.OK);
    }

    @RateLimit(type = "USER")
    @PostMapping(consumes = "application/json", path = "/add")
    @PreAuthorize("#wishlistItem.getWishlist().getUserId() == authentication.principal.getId()")
    public ResponseEntity<WishlistItemDto> addItem(@RequestBody  WishlistItem wishlistItem) {
        WishlistItemDto wi = wishlistItemsService.addItemToWishlist(wishlistItem);
        if (wi == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(wi, HttpStatus.OK);
    }


    @RateLimit(type = "USER")
    @GetMapping( path = "/getByIdWishlist/{idWishlist}/{idUser}")
    @PreAuthorize("#idUser == authentication.principal.getId() or  hasRole('ADMIN')")
    public ResponseEntity<List<WishlistItemDto>>getByWishlist(@PathVariable Long idWishlist, @PathVariable UUID idUser) {
        log.info("Fetching wishlist items for wishlist id: {}", idWishlist);
        List<WishlistItemDto> wishlistItems = wishlistItemsService.getItemsByWishlistId(idWishlist, idUser);

        if (wishlistItems == null || wishlistItems.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(wishlistItems, HttpStatus.OK);
    }


    @RateLimit(type = "USER")
    @GetMapping(consumes = "application/json", path = "/getById/{idItem}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<WishlistItemDto> getById(@PathVariable Long idItem) {
        System.out.println("idItem: " + idItem);
        WishlistItemDto wi = null;
        try {
            wi = wishlistItemsService.getById(idItem);
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


    @RateLimit(type = "USER")
    @DeleteMapping( path = "/delete/{idItem}/{idUser}")
    @PreAuthorize("#idUser == authentication.principal.getId() or hasRole('ADMIN')")
    public ResponseEntity<WishlistItemDto> deleteItem(@PathVariable Long idItem, @PathVariable UUID idUser) {
        WishlistItemDto wi = wishlistItemsService.deleteItemById(idItem, idUser);
        if (wi == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(wi, HttpStatus.OK);
    }
}
