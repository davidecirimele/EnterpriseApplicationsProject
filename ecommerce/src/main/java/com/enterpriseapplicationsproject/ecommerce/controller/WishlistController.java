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
@RequestMapping(value = "/api/v1/wishlists", produces = "application/json")// indica che
@CrossOrigin(origins = "*", allowedHeaders = "*") // indica
@RequiredArgsConstructor
@Slf4j // indica che il logger è di tipo log4j
public class WishlistController {

    private final WishlistsService wishlistService;

    @GetMapping(consumes = "application/json",path= "/getAll")
    public ResponseEntity<List<WishlistDto>> all() {
        List<WishlistDto> wishlists = wishlistService.getAllSorted();
        if (wishlists.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(wishlists, HttpStatus.OK);
    }

    @GetMapping(consumes = "application/json", path = "/get/{idWishlist}")
    public ResponseEntity<WishlistDto> getById(@PathVariable Long id) {
        WishlistDto w = wishlistService.getById(id);
        if(w == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // meglio farlo nel service e gestire l'eccezione con l'handler
        return new ResponseEntity<>(w, HttpStatus.OK);
    }

    @PostMapping(consumes =  "application/json", path = "/add")
    public ResponseEntity<WishlistDto> add(@RequestBody WishlistDto wDto) {
        WishlistDto w = wishlistService.save(wDto);
        if (w == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(w, HttpStatus.OK);
    }

    @PutMapping(consumes =  "application/json", path = "/update/{idWishlist}") // indica che il metodo risponde a una richiesta di tipo PUT
    public ResponseEntity<WishlistDto> update(@PathVariable("idWishlist") Long id, @RequestBody WishlistDto wDto) {
        WishlistDto w = wishlistService.updateWishlist(id,wDto);
        if (w == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(w, HttpStatus.OK);
    }

    @DeleteMapping(path = "/delete/{idWishlist}")
    public ResponseEntity<WishlistDto> delete(@PathVariable("idWishlist") Long id) {
        WishlistDto w = wishlistService.deleteWishlistByID(id);
        return new ResponseEntity<>(w, HttpStatus.OK);
    }
    /*

    @GetMapping("/wishlists/test")
    public ResponseEntity<List<WishlistDto>> test(@RequestParam("name") String name) {
        return ResponseEntity.ok(wishlistService.getByLastname(name));
    }*/



}
