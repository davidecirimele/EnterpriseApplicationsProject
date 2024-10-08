package com.enterpriseapplicationsproject.ecommerce.controller;


import com.enterpriseapplicationsproject.ecommerce.config.security.JwtService;
import com.enterpriseapplicationsproject.ecommerce.config.security.LoggedUserDetails;
import com.enterpriseapplicationsproject.ecommerce.config.security.LoggedUserDetailsService;
import com.enterpriseapplicationsproject.ecommerce.data.entities.Wishlist;
import com.enterpriseapplicationsproject.ecommerce.data.service.WishlistsService;
import com.enterpriseapplicationsproject.ecommerce.data.service.impl.WishlistsServiceImpl;
import com.enterpriseapplicationsproject.ecommerce.dto.WishlistDto;
import com.enterpriseapplicationsproject.ecommerce.dto.security.SharedWishlistRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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


    @GetMapping(path = "/getOfFriend/{idUser}")
    //@PreAuthorize("#idUser == authentication.principal.getId() or hasRole('ADMIN')")
    public  ResponseEntity<List<WishlistDto>> getOfFriend(@PathVariable UUID idUser) {
        List<WishlistDto> w = wishlistService.getWishlistsOfFriend(idUser);
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

    //TO DOO
    @GetMapping(path = "/getFriendWishlists/{idUser}")
    //@PreAuthorize("#idUser == authentication.principal.getId() or hasRole('ADMIN')")
    public ResponseEntity<List<WishlistDto>> getFriendWishlists(@PathVariable UUID idUser) {
        log.info("Received request for friend Wishlists");
        List<WishlistDto> w = wishlistService.getFriendWishlists(idUser);
        if(w == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // meglio farlo nel service e gestire l'eccezione con l'handler
        return new ResponseEntity<>(w, HttpStatus.OK);
    }

    @PostMapping(path = "/share")
    @PreAuthorize("isAuthenticated() or hasRole('ADMIN')")
    public ResponseEntity<Map <String,String> > shareWishlist(@RequestBody SharedWishlistRequest request) {
        // Estrarre i dettagli dell'utente loggato
        LoggedUserDetails userDetails = (LoggedUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Generare il token JWT che include le informazioni della wishlist
        String wishlistSharedToken = jwtService.generateSharedWishlistToken(userDetails, request.getWishlistId(), 1);

        Map<String, String> response = new HashMap<>();
        response.put("token", wishlistSharedToken); // Incapsula il token in una mappa

        System.out.println("Generated wToken: " + wishlistSharedToken);
        return ResponseEntity.ok(response);

    }

    //TO DOs

    @PostMapping(path = "/join/{idUserToJoin}/{token}")
    @PreAuthorize("#idUserToJoin == authentication.principal.getId() or hasRole('ADMIN')")
    public ResponseEntity<String> joinWishlist(@PathVariable UUID idUserToJoin, String token) {
        try {
            // Analizzare il token della wishlist
            SharedWishlistRequest tokenDetails = jwtService.parseWishlistToken(token);

            // Ottenere l'ID della wishlist e l'ID dell'utente dal token
            Long wishlistId = tokenDetails.getWishlistId();
            String ownerUserEmail = tokenDetails.getEmail();

            // Logica per aggiungere l'utente alla wishlist condivisa
            Boolean success = wishlistService.JoinShareWishlist(wishlistId,idUserToJoin);

            return success ? ResponseEntity.ok("Successfully joined the wishlist") :
                    ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to join wishlist");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }
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
