package com.enterpriseapplicationsproject.ecommerce.exception;

public class IsTokenRevokedException extends RuntimeException {
    public IsTokenRevokedException(String message) {
        super(message);
    }
}

