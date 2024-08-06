package com.enterpriseapplicationsproject.ecommerce.exception;

public class InvalidJwtException extends RuntimeException{
    public InvalidJwtException(String message) {
        super(message);
    }
}
