package com.enterpriseapplicationsproject.ecommerce.controller;


import com.enterpriseapplicationsproject.ecommerce.data.service.WishlistsService;
import com.enterpriseapplicationsproject.ecommerce.data.service.impl.WishlistsServiceImpl;
import com.enterpriseapplicationsproject.ecommerce.dto.WishlistDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
        if(w == null)
            return ResponseEntity.notFound().build(); // meglio farlo nel service e gestire l'eccezione con l'handler
        return ResponseEntity.ok(w);
    }

    @PostMapping("/wishlists")
    public ResponseEntity<WishlistDto> add(@RequestBody WishlistDto wDto) {
        WishlistDto w = wishlistService.save(wDto);
        return ResponseEntity.ok(w);
    }

    /*
    @PutMapping("/wishlists/{idWishlist}") // indica che il metodo risponde a una richiesta di tipo PUT
    public ResponseEntity<WishlistDto> update(@PathVariable("idWishlist") Long id, @RequestBody WishlistDto wDto) {
        WishlistDto w = wishlistService.updateWishlist(id,wDto);
        return ResponseEntity.ok(w);
    }*/

    @DeleteMapping("/wishlists/{idWishlist}")
    public HttpStatus delete(@PathVariable("idWishlist") Long id) {
        wishlistService.deleteWishlist(id);
        return HttpStatus.OK;
    }

    @GetMapping("/wishlists/test")
    public ResponseEntity<List<WishlistDto>> test(@RequestParam("name") String name) {
        return ResponseEntity.ok(wishlistService.getByLastname(name));
    }



}
