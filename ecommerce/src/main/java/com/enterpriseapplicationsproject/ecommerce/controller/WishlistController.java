package com.enterpriseapplicationsproject.ecommerce.controller;


import com.enterpriseapplicationsproject.ecommerce.config.security.*;
import com.enterpriseapplicationsproject.ecommerce.data.service.WishlistsService;
import com.enterpriseapplicationsproject.ecommerce.dto.UserIdDto;
import com.enterpriseapplicationsproject.ecommerce.dto.WishlistDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(value = "api/v1/wishlists")// produces indica che
@CrossOrigin(origins = "*", allowedHeaders = "*") // indica
@RequiredArgsConstructor
@Slf4j // indica che il logger è di tipo log4j
public class WishlistController {

    private final WishlistsService wishlistService;

    private final RateLimitingService rateLimitingService;
    private final JwtService jwtService;
    private final LoggedUserDetailsService loggedUserDetailsService;


    @RateLimit//limite di richieste
    @GetMapping(path= "/getAll")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<WishlistDto>> getAll() {
        List<WishlistDto> wishlists = wishlistService.getAllSorted();
        if (wishlists.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(wishlists, HttpStatus.OK);
    }

    @RateLimit(type = "USER")
    @GetMapping(path = "/get/{idWishlist}")
    @PreAuthorize("isAuthenticated() or hasRole('ADMIN')")
    public ResponseEntity<WishlistDto> getById(@PathVariable Long idWishlist) {
        WishlistDto w = wishlistService.getDtoById(idWishlist);
        if(w == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(w, HttpStatus.OK);
    }


    @RateLimit(type = "USER")
    @GetMapping(path = "/getByUser/{idUser}")
    @PreAuthorize("#idUser == authentication.principal.getId() or hasRole('ADMIN')")
    public ResponseEntity<List<WishlistDto>> getByUser(@PathVariable UUID idUser) {
        log.info("Received request for addresses/{idUser}");
        List<WishlistDto> w = wishlistService.getWishlistsByUser(idUser);
        if(w == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(w, HttpStatus.OK);
    }


    @RateLimit(type = "USER")
    @PostMapping(consumes = "application/json", path = "/add")
    @PreAuthorize("#wDto.getUser().getId()  == authentication.principal.getId() or hasRole('ADMIN')")
    public ResponseEntity<WishlistDto> add(@RequestBody WishlistDto wDto) {
        WishlistDto w = wishlistService.save(wDto);
        if (w == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(w, HttpStatus.OK);
    }


    //TO DOO
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


    //TO test
    @RateLimit(type = "USER")
    @PostMapping(path = "/join/{idUser}/{token}")
    @PreAuthorize("#idUser == authentication.principal.getId() or hasRole('ADMIN')")
    public ResponseEntity <Boolean> joinWishlist(@PathVariable UUID idUser, @PathVariable String token) {
        Boolean resp = wishlistService.JoinShareWishlist(idUser, token);
        if (resp){
            return new ResponseEntity<>(resp, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND); // meglio farlo nel service e gestire l'eccezione con l'handler
    }


    //TO DO
    @RateLimit(type = "USER")
    @PostMapping(path = "/unshare/{idUser}")
    @PreAuthorize("#idUser == authentication.principal.getId() or hasRole('ADMIN')")
    public ResponseEntity<Boolean> unshare(@PathVariable UUID idUser, @RequestBody WishlistDto wDto) {
        boolean resp = wishlistService.unshareWishlist(wDto.getId(), idUser);
        if (resp){
            return new ResponseEntity<>(resp, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    //


    @RateLimit(type = "USER")
    @PutMapping(consumes = "application/json", path = "/update")
    @PreAuthorize("#wDto.getUser().getId() == authentication.principal.getId() or hasRole('ADMIN')")
    public ResponseEntity<WishlistDto> update(@RequestBody WishlistDto wDto) {
        System.out.println("WishlistDto userID: " + wDto.getUser().getId());
        WishlistDto w = wishlistService.updateWishlist(wDto);
        if (w == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(w, HttpStatus.OK);
    }


    @RateLimit(type = "USER")
    @DeleteMapping(path = "/delete/{idWishlist}/{idUser}")
    @PreAuthorize("#idUser == authentication.principal.getId() or hasRole('ADMIN')")
    public ResponseEntity<WishlistDto> deleteById(@PathVariable Long idWishlist, @PathVariable UUID idUser) {
        WishlistDto w = wishlistService.deleteWishlistByID(idWishlist, idUser);
        if(w == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(w, HttpStatus.OK);
    }


}
