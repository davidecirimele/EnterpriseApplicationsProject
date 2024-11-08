package com.enterpriseapplicationsproject.ecommerce.controller;

import com.enterpriseapplicationsproject.ecommerce.config.security.RateLimit;
import com.enterpriseapplicationsproject.ecommerce.data.entities.User;
import com.enterpriseapplicationsproject.ecommerce.data.service.*;
import com.enterpriseapplicationsproject.ecommerce.dto.*;
import com.enterpriseapplicationsproject.ecommerce.dto.security.RefreshTokenDto;
import com.enterpriseapplicationsproject.ecommerce.exception.UserRegistrationException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("api/v1/admin")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class AdminController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final OrdersService ordersService;
    private final WishlistItemsService wishlistItemsService;
    private final WishlistsService wishlistService;


    @RateLimit
    @PostMapping(consumes = "application/json", path = "/register")
    public ResponseEntity<UserDetailsDto> registerAdmin(@Valid @RequestBody  SaveUserDto userDto) {
        return ResponseEntity.ok(authService.registerAdmin(userDto));
    }

    @RateLimit(type ="USER")
    @GetMapping("/all-tokens")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RefreshTokenDto>> allTokens() {
        List<RefreshTokenDto> tokens = refreshTokenService.getAll();
        return new ResponseEntity<>(tokens, HttpStatus.OK);
    }

    @GetMapping("/all-orders")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<OrderSummaryDto>> allOrders( @RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<OrderSummaryDto> orders = ordersService.getAll(pageable);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @RateLimit(type = "USER")
    @PutMapping(consumes = "application/json", path = "/update/{idUser}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<WishlistDto> update(@RequestBody WishlistDto wDto, @PathVariable UUID idUser) {
        System.out.println("WishlistDto userID: " + wDto.getUser().getId());
        WishlistDto w = wishlistService.updateWishlist(wDto, idUser);
        if (w == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(w, HttpStatus.OK);
    }

    @RateLimit(type = "USER")
    @PostMapping(path = "/addWishlistItem/{idBook}/{idWishlist}/{idUser}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<WishlistItemDto> addItem(@PathVariable  Long idBook, @PathVariable Long idWishlist, @PathVariable UUID idUser) {
        WishlistItemDto wi = wishlistItemsService.adminAddItem(idBook, idWishlist, idUser);
        if (wi == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(wi, HttpStatus.OK);
    }

    @RateLimit(type = "USER")
    @DeleteMapping( path = "/removeWishlist/delete/{idItem}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<WishlistItemDto> deleteItem(@PathVariable Long idItem) {
        WishlistItemDto wi = wishlistItemsService.adminDeleteItemById(idItem);
        if (wi == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(wi, HttpStatus.OK);
    }





}
