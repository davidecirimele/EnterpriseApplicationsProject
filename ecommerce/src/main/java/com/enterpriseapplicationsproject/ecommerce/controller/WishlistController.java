package com.enterpriseapplicationsproject.ecommerce.controller;


import com.enterpriseapplicationsproject.ecommerce.data.service.WishlistsService;
import com.enterpriseapplicationsproject.ecommerce.data.service.impl.WishlistsServiceImpl;
import com.enterpriseapplicationsproject.ecommerce.dto.WishlistDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wishlist-api")// indica che
@CrossOrigin(origins = "*", allowedHeaders = "*") // indica
@RequiredArgsConstructor
@Slf4j // indica che il logger è di tipo log4j
public class WishlistController {

    private final WishlistsService wishlistService;

    @GetMapping("/wishlists")
    public ResponseEntity<List<WishlistDto>> all() {
        return ResponseEntity.ok(wishlistService.getAllSorted());
    }

    @GetMapping("/wishlists/{idWishlist}")
    public ResponseEntity<WishlistDto> getById(@PathVariable("idWishlist") Long id) {
        WishlistDto w = wishlistService.getById(id);
        if(w==null)
            return ResponseEntity.notFound().build(); // meglio farlo nel service e gestire l'eccezione con l'handler
        return ResponseEntity.ok(w);
    }

    @GetMapping("/wishlists/test")
    public ResponseEntity<List<WishlistDto>> test(@RequestParam("name") String name) {
        return ResponseEntity.ok(wishlistService.getByLastname(name));
    }



}
