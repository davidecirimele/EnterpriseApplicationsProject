package com.enterpriseapplicationsproject.ecommerce.dto.security;


import lombok.Data;

import java.util.UUID;

@Data
public class SharedWishlistRequest {

    private String email;
    private Long wishlistId;

    public SharedWishlistRequest(String email, Long wishlistId ){
        this.email = email;
        this.wishlistId = wishlistId;
    }


}
