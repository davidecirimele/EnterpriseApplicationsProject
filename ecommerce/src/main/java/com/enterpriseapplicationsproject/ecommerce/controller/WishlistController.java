package com.enterpriseapplicationsproject.ecommerce.controller;


import com.enterpriseapplicationsproject.ecommerce.config.security.JwtService;
import com.enterpriseapplicationsproject.ecommerce.config.security.LoggedUserDetailsService;
import com.enterpriseapplicationsproject.ecommerce.data.entities.Wishlist;
import com.enterpriseapplicationsproject.ecommerce.data.service.WishlistsService;
import com.enterpriseapplicationsproject.ecommerce.data.service.impl.WishlistsServiceImpl;
import com.enterpriseapplicationsproject.ecommerce.dto.WishlistDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "api/v1/wishlists")// produces indica che
@CrossOrigin(origins = "*", allowedHeaders = "*") // indica
@RequiredArgsConstructor
@Slf4j // indica che il logger è di tipo log4j
public class WishlistController {

    private final WishlistsService wishlistService;

    private final JwtService jwtService;
    private final LoggedUserDetailsService loggedUserDetailsService;

    @GetMapping(path= "/getAll")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<WishlistDto>> getAll() {
        List<WishlistDto> wishlists = wishlistService.getAllSorted();
        if (wishlists.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(wishlists, HttpStatus.OK);
    }

    @GetMapping(path = "/get/{idWishlist}")
    @PreAuthorize("isAuthenticated() or hasRole('ADMIN')")
    public ResponseEntity<WishlistDto> getById(@PathVariable Long idWishlist) {
        WishlistDto w = wishlistService.getById(idWishlist);
        if(w == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // meglio farlo nel service e gestire l'eccezione con l'handler
        return new ResponseEntity<>(w, HttpStatus.OK);
    }


    @GetMapping(path = "/getByUser/{idUser}")
    @PreAuthorize("#idUser == authentication.principal.getId() or hasRole('ADMIN')")
    public ResponseEntity<List<WishlistDto>> getByUser(@PathVariable UUID idUser) {
        log.info("Received request for addresses/{idUser}");
        List<WishlistDto> w = wishlistService.getWishlistsByUser(idUser);
        if(w == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // meglio farlo nel service e gestire l'eccezione con l'handler
        return new ResponseEntity<>(w, HttpStatus.OK);
    }

    @PostMapping(consumes =  "application/json", path = "/add")
    //@PreAuthorize("#wDto.getUser().getId()  == authentication.principal.getId() or hasRole('ADMIN')")
    public ResponseEntity<WishlistDto> add(@RequestBody WishlistDto wDto) {
        WishlistDto w = wishlistService.save(wDto);
        if (w == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(w, HttpStatus.OK);
    }

    @PostMapping(consumes = "application/json", path = "/share")
    //@PreAuthorize("#idUser == authentication.principal.getId() or hasRole('ADMIN')")
    public ResponseEntity<String> share(@RequestBody WishlistDto wDto) {

        String username = jwtService.extractUsername(wDto.getUser().getFirstName());

        final UserDetails userDetails = loggedUserDetailsService.loadUserByUsername(username);

        String wishlistSharedToken = jwtService.generateSharedWishlistToken( userDetails,wDto,1);

        return new ResponseEntity<>(wishlistSharedToken, HttpStatus.OK);
    }
    //TO DOs

    @PostMapping(consumes = "application/json", path = "/joinShared/{idUser}/{token}")
    //@PreAuthorize("#idUser == authentication.principal.getId() or hasRole('ADMIN')")
    public ResponseEntity<Boolean> joinShared(@PathVariable UUID idUser, @PathVariable String token) {
        String toDo = "TOdo";
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @PostMapping(consumes = "application/json", path = "/unshare")
    //@PreAuthorize("#idUser == authentication.principal.getId() or hasRole('ADMIN')")
    public ResponseEntity<String> unshare(@RequestBody WishlistDto wDto) {
        String toDo = "TOdo";
        return new ResponseEntity<>(toDo, HttpStatus.OK);
    }
    //

    @PutMapping(consumes = "application/json", path = "/update")
    //@PreAuthorize("#wDto.getUser().getId() == authentication.principal.getId() or hasRole('ADMIN')")
    public ResponseEntity<WishlistDto> update(@RequestBody WishlistDto wDto) {
        WishlistDto w = wishlistService.updateWishlist(wDto);
        if (w == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(w, HttpStatus.OK);
    }



    @DeleteMapping(path = "/delete/{idWishlist}")
    //@PreAuthorize("isAuthenticated() or hasRole('ADMIN')")
    public ResponseEntity<WishlistDto> deleteById(@PathVariable Long idWishlist) {
        WishlistDto w = wishlistService.deleteWishlistByID(idWishlist);
        if(w == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(w, HttpStatus.OK);
    }
    /*

    @GetMapping("/wishlists/test")
    public ResponseEntity<List<WishlistDto>> test(@RequestParam("name") String name) {
        return ResponseEntity.ok(wishlistService.getByLastname(name));
    }*/



}
