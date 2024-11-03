package com.enterpriseapplicationsproject.ecommerce.exception;

public class WishlistCantBeDeletedException extends RuntimeException{

    public WishlistCantBeDeletedException(String message) {
        super(message);
    }
}
