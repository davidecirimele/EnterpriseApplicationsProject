package com.enterpriseapplicationsproject.ecommerce.controller;


import com.enterpriseapplicationsproject.ecommerce.config.security.*;
import com.enterpriseapplicationsproject.ecommerce.config.security.JwtService;
import com.enterpriseapplicationsproject.ecommerce.config.security.LoggedUserDetails;
import com.enterpriseapplicationsproject.ecommerce.config.security.LoggedUserDetailsService;
import com.enterpriseapplicationsproject.ecommerce.data.entities.WishlistPrivacy;
import com.enterpriseapplicationsproject.ecommerce.data.service.WishlistsService;
import com.enterpriseapplicationsproject.ecommerce.dto.SaveWishlistDto;
import com.enterpriseapplicationsproject.ecommerce.dto.UserIdDto;
import com.enterpriseapplicationsproject.ecommerce.dto.WishlistDto;
import com.enterpriseapplicationsproject.ecommerce.dto.security.SharedWishlistRequest;
import com.enterpriseapplicationsproject.ecommerce.dto.security.SharedWishlistRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/v1/wishlists")// produces indica che
@CrossOrigin(origins = "*", allowedHeaders = "*") // indica
@RequiredArgsConstructor
@Slf4j // indica che il logger è di tipo log4j
public class WishlistController {

    private final WishlistsService wishlistService;

    private final RateLimitingService rateLimitingService;
    private final JwtService jwtService;
    private final LoggedUserDetailsService loggedUserDetailsService;


    @RateLimit
    @GetMapping(path= "/getAll")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<WishlistDto>> getAll() {
        List<WishlistDto> wishlists = wishlistService.getAllSorted();
        if (wishlists == null || wishlists.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(wishlists, HttpStatus.OK);
    }

    @RateLimit(type = "USER")
    @GetMapping(path = "/get/{idWishlist}/{idUser}")
    @PreAuthorize("#idUser == authentication.principal.getId() or hasRole('ADMIN')")
    public ResponseEntity<WishlistDto> getById(@PathVariable Long idWishlist, @PathVariable UUID idUser) {
        WishlistDto w = wishlistService.getDtoById(idWishlist);
        if(w == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // meglio farlo nel service e gestire l'eccezione con l'handler
        return new ResponseEntity<>(w, HttpStatus.OK);
    }


    @RateLimit(type = "USER")
    @GetMapping(path = "/getByUser/{idUser}")
    @PreAuthorize("#idUser == authentication.principal.getId() or hasRole('ADMIN')")
    public ResponseEntity<List<WishlistDto>> getByUser(@PathVariable UUID idUser) {
        log.info("Received request for addresses/{idUser}");
        List<WishlistDto> w = wishlistService.getWishlistsByUser(idUser);
        if(w == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // meglio farlo nel service e gestire l'eccezione con l'handler
        return new ResponseEntity<>(w, HttpStatus.OK);
    }


    @RateLimit(type = "USER")
    @PostMapping( path = "/add/{idUser}/{wName}/{wPrivacySetting}")
    @PreAuthorize("#idUser == authentication.principal.getId() or hasRole('ADMIN')")
    public ResponseEntity<Boolean> addWishlist(@PathVariable UUID idUser, @PathVariable String wName, @PathVariable WishlistPrivacy wPrivacySetting) {
        System.out.println("Add wishlist per utente " + idUser);
        System.out.println("Nome wishlist: " + wName);

        WishlistDto w = wishlistService.save(idUser, wName, wPrivacySetting);
        if (w == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(true, HttpStatus.OK);
    }


    @RateLimit(type = "USER")
    @GetMapping(path = "/getOfFriend/{idUser}")
    @PreAuthorize("#idUser == authentication.principal.getId() or hasRole('ADMIN')")
    public ResponseEntity<List<WishlistDto>> getFriendWishlists(@PathVariable UUID idUser) {
        log.info("Received request for friend Wishlists");
        List<WishlistDto> w = wishlistService.getFriendWishlists(idUser);
        if(w == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // meglio farlo nel service e gestire l'eccezione con l'handler
        return new ResponseEntity<>(w, HttpStatus.OK);
    }


    @RateLimit(type = "USER")
    @PutMapping(consumes = "application/json", path = "/update/{idUser}")//Controlli nel service
    public ResponseEntity<WishlistDto> update(@RequestBody WishlistDto wDto, @PathVariable UUID idUser) {
        System.out.println("WishlistDto userID: " + wDto.getUser().getId());
        WishlistDto w = wishlistService.updateWishlist(wDto, idUser);
        if (w == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(w, HttpStatus.OK);
    }


    @RateLimit(type ="USER")
    @DeleteMapping(path = "/delete/{idWishlist}/{idUser}")
    @PreAuthorize("#idUser == authentication.principal.getId() or hasRole('ADMIN')")
    public ResponseEntity<WishlistDto> deleteById(@PathVariable Long idWishlist, @PathVariable UUID idUser) {
        WishlistDto w = wishlistService.deleteWishlistByID(idWishlist, idUser);
        if(w == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(w, HttpStatus.OK);
    }


}
